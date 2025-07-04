import {defineStore} from 'pinia'
import type Deck from "@/types/Deck";
import type Card from "@/types/Card"
import axios from "../api/config";
import {useUserStore} from "@/stores/users";

export const useDeckStore = defineStore('decks', {
  state: () => ({
    decks: [] as Deck[],
    decksLoading: 0,
    abortController: null as AbortController | null
  }),

  actions: {
    loadFromLocalStorage() {
      this.decks = [];
      const data = localStorage.getItem('decks')
      if (data) {
        this.decks = JSON.parse(data)
      }
    },
    getloadingDecks(){
      return this.decksLoading
    },
    abortDeckLoading() {
      if (this.abortController) {
        this.abortController.abort();
        this.abortController = null;
      }
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
      this.abortDeckLoading();
      this.abortController = new AbortController();
      const signal = this.abortController.signal;
      if (decks.length <= 0){
        return
      }
      this.decksLoading = decks.length
      for (const [deckname, color] of decks) {
        try {
          await this.addDeck(deckname, color, signal);
        } catch (e) {
          if (signal.aborted) {
            console.log(`Aborted loading deck ${deckname}`);
          } else {
            console.error(e);
          }
        }
      }
    },

    async addDeck(deckname: string,color: string|undefined,signal: AbortSignal): Promise<void>{
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
      const response = await axios.get('api/decks',{signal})
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
        const cards = await axios.get('/api/decks/' + id + '/cards',{signal})
          for(const card of cards.data){
            let  LastRating
            let nextRep = undefined
            if(useUserStore().authenticated){
              const info = await axios.get('/api/usr/decks/' + id + '/cards/' + card.cardId + '/info',{signal})

              nextRep = info.data.nextRepetition

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
                lastRating:LastRating,
                nextRepetition:nextRep
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
                lastRating:LastRating,
                nextRepetition:nextRep
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
                lastRating:LastRating,
                nextRepetition:nextRep
              }
              problems.push(newCard)            }
          }
        if(useUserStore().authenticated){
          await axios.post('/api/usr/decks/' + id + '/add',{signal})
        }
      }

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

    async deactivateDeck(deckname: string): Promise<void>{
      let counter: number = 0
      while(counter < this.decks.length){
        const deck = this.decks[counter]
        if (deck.title === deckname) {
          for(const id of deck.stapel_id){
            await axios.delete('/api/usr/decks/' + id + '/delete')
          }
          this.decks.splice(counter,1)
          localStorage.setItem('decks', JSON.stringify(this.decks));
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
      const date = new Date();

      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const year = date.getFullYear();
      const currentDate = `${year}-${month}-${day}`;

      let faellig = 0
      let Cards: Card[] = []
      Cards = Cards.concat(deck.definitions)
      Cards = Cards.concat(deck.schemas)
      Cards = Cards.concat(deck.problems)
      for(const curCard of Cards)
      {

        if(curCard.nextRepetition === currentDate){
          faellig += 1
        }
      }
      return faellig
    },

    async resetCards(deckName : string): Promise<void>{
      for(const deck of this.decks){
        if(deck.title === deckName){
          for(const card of deck.schemas){
            if (useUserStore().authenticated) {
              await axios.patch(
                '/api/usr/decks/' + card.deckID + '/' + card.id + '/rank',
                "NOT_LEARNED",
                { headers: { "Content-Type": "text/plain" } }
              );
            }
            card.lastRating = 4
            const newInfo = await axios.get('/api/usr/decks/' + card.deckID + '/cards/' + card.id + '/info')
            card.nextRepetition = newInfo.data.nextRepetition;
          }
          for(const card of deck.problems){
            if (useUserStore().authenticated) {
              await axios.patch(
                '/api/usr/decks/' + card.deckID + '/' + card.id + '/rank',
                "NOT_LEARNED",
                { headers: { "Content-Type": "text/plain" } }
              );
            }
            card.lastRating = 4
            const newInfo = await axios.get('/api/usr/decks/' + card.deckID + '/cards/' + card.id + '/info')
            card.nextRepetition = newInfo.data.nextRepetition;
          }
          for(const card of deck.definitions){
            if (useUserStore().authenticated) {
              await axios.patch(
                '/api/usr/decks/' + card.deckID + '/' + card.id + '/rank',
                "NOT_LEARNED",
                { headers: { "Content-Type": "text/plain" } }
              );
            }
            card.lastRating = 4
            const newInfo = await axios.get('/api/usr/decks/' + card.deckID + '/cards/' + card.id + '/info')
            card.nextRepetition = newInfo.data.nextRepetition;
          }
        }
      }
    },

    getCardNumber(deck:Deck):number{
      return deck.schemas.length + deck.problems.length + deck.definitions.length
    },

    async loadMyDecks() {
      this.decks = [];
      localStorage.removeItem('decks');
      const tuples = await axios.get('api/usr/activeDecks');
      this.abortDeckLoading()
      await this.addMultDecks(tuples.data);
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
      this.abortDeckLoading()
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

    async rate(cardID: number, deckID: number, rateIndex: number) {
      const deck = this.getDeckByOneID(deckID);
      if (deck) {
        const allCards = deck.problems.concat(deck.schemas, deck.definitions);
        for (const card of allCards) {
          if (card.id == cardID) {
            card.lastRating = rateIndex;

            if (useUserStore().authenticated) {
              let rankString = "";
              switch (rateIndex) {
                case 0: rankString = "EASY"; break;
                case 1: rankString = "GOOD"; break;
                case 2: rankString = "HARD"; break;
                case 3: rankString = "AGAIN"; break;
                case 4: rankString = "NOT_LEARNED"; break;
              }

              await axios.patch(
                '/api/usr/decks/' + card.deckID + '/' + card.id + '/rank',
                rankString,
                { headers: { "Content-Type": "text/plain" } }
              );

              const newInfo = await axios.get('/api/usr/decks/' + card.deckID + '/cards/' + card.id + '/info')
              card.nextRepetition = newInfo.data.nextRepetition;
            }

            break;
          }
        }
      }

      localStorage.setItem('decks', JSON.stringify(this.decks));
    }

  },
})
