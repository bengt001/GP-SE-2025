import {defineStore} from 'pinia'
import type Deck from "@/types/Deck";
import axios from "../api/config";

export const useDeckStore = defineStore('decks', {
  state: () => ({
    decks: JSON.parse(localStorage.getItem('decks') || '[]') as Deck[],
  }),

  actions: {
    async addDeck(deckname: string,color: string|undefined): Promise<void>{
      const exists = this.decks.some(deck => deck.title === deckname);
      if (exists){
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

      let definitonCount:number = 0;
      let problemCount:number = 0;
      let schemaCount:number = 0;

      const IDlist = []
      const response = await axios.get('api/decks')
      const allDecks = response.data
      for(const deck of allDecks){
        if(deck.fieldOfLaw.includes(nameSplit[0]) && deck.authorId === authorID){
          IDlist.push(deck.deckId)
        }
      }

      if(IDlist.length === 0){
        return
      }

      for(const id of IDlist){
        const cards = await axios.get('api/decks/' + id + '/cards')
          for(const card of cards.data){
            if(card.cardType == "Definitionen"){
              definitonCount += 1
            }
            else if(card.cardType == "Aufdeckkarte"){
              schemaCount += 1
            }
            else if(card.cardType == "Probleme"){
              problemCount += 1
            }
          }
      }


      //TODO Deck beim user speichern
      const cards_arr = [0,0,0,0,20]  //TODO ratings aus backend
      const newDeck: Deck = {
        title: deckname,
        author_id: authorID,
        stapel_id: IDlist,
        definitions: definitonCount,
        problems: problemCount,
        schemas: schemaCount,
        cards: cards_arr,
        color: color
      }
      this.decks.push(newDeck)
      localStorage.setItem('decks', JSON.stringify(this.decks));
    },

    //TODO ENTFERNEN nur für Präsentation
    setProgress(deckID : number[]): void{
      for(const ID of deckID){
        for(const deck of this.decks){
          if(deck.stapel_id.includes(ID)){
            deck.cards = [1,2,1,1,15]
          }
        }
      }
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
          let cardNumber : number = 0
          cardNumber = this.getCardNumber(deck.cards)
          deck.cards = [0,0,0,0,cardNumber]         //TODO im backend deck resetten
        }
      }
    },
    getCardNumber(cards:number[]):number{
      let count:number = 0
      for(const card of cards){
        count += card
      }
      return count
    },
    async get_my_active_decks(): Promise<void>{
      //MOCK um es zu leeren
      this.clear_decks();
      await this.addDeck("Strafrecht AT (Lexmea)","#03364D")
      this.setDeckProgress("Strafrecht AT (Lexmea)",[5,3,2,4,5])
      //TODO MOCK entfernen
    },
    //MOCK wahrscheinlich nicht benötigt--> entfernen
    setDeckProgress(deckname: string,progress: number[]): void{
      for(const deck of this.decks){
        if(deck.title === deckname){
          deck.cards = progress
        }
      }
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

      const IDlist = []
      const response = await axios.get('api/decks')
      const allDecks = response.data
      for(const deck of allDecks){
        if(deck.fieldOfLaw.includes("Strafrecht AT") && deck.authorId === 1){
          IDlist.push(deck.deckId)
        }
      }

      let definitonCount:number = 0;
      let problemCount:number = 0;
      let schemaCount:number = 0;

      for(const id of IDlist){
        const cards = await axios.get('api/decks/' + id + '/cards')
        for(const card of cards.data){
          if(card.cardType == "Definitionen"){
            definitonCount += 1
          }
          else if(card.cardType == "Aufdeckkarte"){
            schemaCount += 1
          }
          else if(card.cardType == "Probleme"){
            problemCount += 1
          }
        }
      }

      const cards_arr = [0,0,0,0,5] //TODO aus dem backend bekommen
      const newDeck: Deck = {
        title: "Strafrecht AT (Lexmea)",
        author_id: 1,
        stapel_id: IDlist,
        cards: cards_arr,
        definitions: definitonCount,
        problems: problemCount,
        schemas: schemaCount,
        color: "#03364D"
      }
      this.decks.push(newDeck)
      localStorage.setItem('decks', JSON.stringify(this.decks));
    },
    getDecksColor(): string[]{
      const ColorArr:string[] = []
      for(const deck of this.decks){
        const color: string|undefined = deck.color
        ColorArr.push(color ?? "")
      }
      return ColorArr
    }

  },
})
