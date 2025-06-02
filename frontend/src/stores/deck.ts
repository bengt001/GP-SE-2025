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
          authorID = 1
          break
        case "Lexmea)":
          authorID = 2
          break
        case "Eigener Stapel)":
          authorID = 3
          break
        default:
          break
      }

      const IDlist = []
      const response = await axios.get('api/decks')
      const allDecks = response.data
      for(const deck of allDecks){
        if(deck.fieldOfLaw.includes(nameSplit[0]) && deck.authorId === authorID){
          IDlist.push(deck.deckId)
        }
      }



      const cards_arr = [0,0,0,0,5]  //TODO ratings aus backend
      const newDeck: Deck = {
        title: deckname,
        author_id: authorID,
        stapel_id: IDlist,
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
            deck.cards = [1,2,1,1,0]
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
      return deck.stapel_id[0]
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
    get_my_active_decks(): void{
      //MOCK um es zu leeren
      const counter: number = 0
      while (counter < this.decks.length){
        this.decks.splice(counter ,1)
        localStorage.setItem('decks',JSON.stringify(this.decks))
      }
      //TODO MOCK entfernen
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
        if(deck.fieldOfLaw.includes("Strafrecht AT") && deck.authorId === 2){
          IDlist.push(deck.deckId)
        }
      }

      const cards_arr = [0,0,0,0,5] //TODO aus dem backend bekommen
      const newDeck: Deck = {
        title: "Strafrecht AT (Lexmea)",
        author_id: 2,
        stapel_id: IDlist,
        cards: cards_arr,
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
