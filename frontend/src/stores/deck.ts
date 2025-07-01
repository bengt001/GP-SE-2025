import {defineStore} from 'pinia'
import type Deck from "@/types/Deck";
import type Card from "@/types/Card"
import axios from "../api/config";
import {useUserStore} from "@/stores/users";

export const useDeckStore = defineStore('decks', {
  state: () => ({
    decks: JSON.parse(localStorage.getItem('decks') || '[]') as Deck[],
    decksLoading: 0
  }),

  actions: {
    getloadingDecks(){
      return this.decksLoading
    },
    getDeckRating(deck:Deck){
      const ratingArr:number[] = [0,0,0,0,0]
      const allCards:Card[] = deck.definitions.concat(deck.schemas,deck.problems)
      for(const card of allCards){
        ratingArr[card.lastRating] += 1
      }
      return ratingArr
    },

    async addMultDecks(decks: [string, string | undefined][]): Promise<void> {
      this.decksLoading = decks.length
      for (const [deckname, color] of decks) {
        await this.addDeck(deckname, color);
      }
    },

    async addDeck(deckname: string,color: string|undefined): Promise<void>{
      const exists = this.decks.some(deck => deck.title === deckname);
      if (exists){
        this.decksLoading--
        return
      }
      const nameSplit = deckname.split(" (")
      let authorID = 0
      switch (nameSplit[1]){
        case "Broadcast)":
          authorID = 0
          break
        case "Lexmea)":
          authorID = 1
          break
        case "Eigener Stapel)":
          authorID = 2
          break
        default:
          break
      }

      const definitons:Card[] = []
      const problems:Card[] = []
      const schemas:Card[] = []

      const IDlist = []
      const response = await axios.get('api/decks')
      const allDecks = response.data
      for(const deck of allDecks){
        if(deck.fieldOfLaw.includes(nameSplit[0]) && deck.authorId === authorID){
          IDlist.push(deck.deckId)
        }
      }

      if(IDlist.length === 0){
        this.decksLoading--
        return
      }


      for(const id of IDlist){
        const cards = await axios.get('api/decks/' + id + '/cards')
          for(const card of cards.data){
            let  LastRating
            if(useUserStore().authenticated){
              console.log("vor dem await")
              const info = await axios.get('api/usr/decks/' + id + '/cards/' + card.cardId + '/info')
              console.log("info: ",info.data)

              switch (info.data.rating) {
                case "NOT_LEARNED":
                  LastRating = 4;
                  break;
                case "AGAIN":
                  LastRating = 3;
                  break;
                case "HARD":
                  LastRating = 2;
                  break;
                case "GOOD":
                  LastRating = 1;
                  break;
                case "EASY":
                  LastRating = 0;
                  break;
                default:
                  LastRating = 4;
                  break;
              }

            }
            else{
              LastRating = 4
            }
            if(card.cardType == "Definitionen"){
              const cardContent = this.cleanDefinitionString(card.content)
              const newCard: Card = {
                id:card.cardId,
                deckID:id,
                type:card.cardType,
                title:cardContent[0],
                text:cardContent[1],
                color:color,
                lastRating:LastRating
              }
              definitons.push(newCard)
            }
            else if(card.cardType == "Aufdeckkarte"){
              const cardContent = this.cleanDefinitionString(card.content)
              const newCard: Card = {
                id:card.cardId,
                deckID:id,
                type:card.cardType,
                title:cardContent[0],
                text:cardContent[1],
                color:color,
                lastRating:LastRating
              }
              schemas.push(newCard)            }
            else if(card.cardType == "Probleme"){
              const cardContent = this.cleanDefinitionString(card.content)
              const newCard: Card = {
                id:card.cardId,
                deckID:id,
                type:card.cardType,
                title:cardContent[0],
                text:cardContent[1],
                color:color,
                lastRating:LastRating
              }
              problems.push(newCard)            }
          }
      }

      //TODO Deck beim user speichern
      const newDeck: Deck = {
        title: deckname,
        author_id: authorID,
        stapel_id: IDlist,
        definitions: definitons,
        problems: problems,
        schemas: schemas,
        color: color
      }
      this.decks.push(newDeck)
      localStorage.setItem('decks', JSON.stringify(this.decks));
      this.decksLoading--
    },

    deactivateDeck(deckname: string): void{
      let counter: number = 0
      while(counter < this.decks.length){
        const deck = this.decks[counter]
        if (deck.title === deckname) {
          this.decks.splice(counter,1)
          localStorage.setItem('decks', JSON.stringify(this.decks)); //TODO im backend vom user entfernen
        }
        counter++
      }
    },

    getAllDecks(): Deck[]{
      return this.decks
    },
    getTitleOfSelected(selected :Deck[]): string {
      const selectedTitles = []
      for (const deck of selected){
        selectedTitles.push(deck.title)
      }
      return selectedTitles.join(",")
    },
    getFaellig(deck: Deck): number{
      return deck.stapel_id.length
    },
    resetCards(deckName : string): void{
      for(const deck of this.decks){
        if(deck.title === deckName){
          //TODO im backend deck resetten
          for(const card of deck.schemas){
            card.lastRating = 4
          }
          for(const card of deck.problems){
            card.lastRating = 4
          }
          for(const card of deck.definitions){
            card.lastRating = 4
          }
        }
      }
    },
    getCardNumber(deck:Deck):number{
      return deck.schemas.length + deck.problems.length + deck.definitions.length
    },
    async get_my_active_decks(): Promise<void>{
      //MOCK um es zu leeren
      this.clear_decks();
      await this.addMultDecks([["Strafrecht AT (Lexmea)", "#03364D"]]);
    },
    clear_decks(): void {
      this.decks.splice(0, this.decks.length)
      localStorage.setItem('decks', JSON.stringify(this.decks))
    },
    async reset_decks(): Promise<void>{
      const counter: number = 0
      while (counter < this.decks.length){
        this.decks.splice(counter ,1)
        localStorage.setItem('decks',JSON.stringify(this.decks))
      }
      await this.addMultDecks([["Strafrecht AT (Lexmea)", "#03364D"]]);
    },
    getDeckByOneID(id:number):Deck|undefined{
      for(const deck of this.decks){
        for(const stapelID of deck.stapel_id){
          if(stapelID === id){
            return deck
          }
        }
      }
      return undefined
    },

    cleanDefinitionString(content: string): string[] {
      const cleanString : string[] = []

      const cardContent = content.split(",")
      const question = cardContent[0]
      const questionCut = question.split("[")
      const cleanQuestion = questionCut[1]

      cleanString.push(cleanQuestion)

      let cardText : string = ""
      for (let i = 1; i < cardContent.length; i++) {
        cardText = cardText.concat(cardContent[i])
      }

      const answer = cardText.split(" \\")
      const cleanAnswer = answer[0].concat("\"")

      cleanString.push(cleanAnswer)

      return cleanString
    },
  //   Funktionene um bei unangemeldeten bewertungen zu speicher:
    rate(cardID:number,deckID:number,rateIndex:number){
      const deck = this.getDeckByOneID(deckID)
      if(deck){
        const allCards = deck.problems.concat(deck.schemas,deck.definitions)
        for(const card of allCards){
          if(card.id == cardID){
            card.lastRating = rateIndex
            break
          }
        }
        localStorage.setItem('decks', JSON.stringify(this.decks))
      }
    },

  },
})
