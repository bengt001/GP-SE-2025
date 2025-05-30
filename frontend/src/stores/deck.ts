import {defineStore} from 'pinia'
import type Deck from "@/types/Deck";

export const useDeckStore = defineStore('decks', {
  state: () => ({
    decks: JSON.parse(localStorage.getItem('decks') || '[]') as Deck[],
  }),

  actions: {
    addDeck(deckname: string,color: string|undefined): void{
      const exists = this.decks.some(deck => deck.title === deckname);
      if (exists){
        return
      }
      const deckId = this.decks.length + 1 //TODO deckID aus backend bekommen
      const authorID = 1 //TODO authorID aus backebnd bekommen
      const visible = true //TODO im backend visibility des decks setzen
      const cards_arr = [1,2,3,4,5]
      const newDeck: Deck = {
        title: deckname,
        author_id: authorID,
        stapel_id: deckId,
        visibility: visible,
        cards: cards_arr,
        color: color
      }
      this.decks.push(newDeck)
      localStorage.setItem('decks', JSON.stringify(this.decks));
    },

    deactivateDeck(deckname: string): void{
      let counter: number = 0
      while(counter < this.decks.length){
        const deck = this.decks[counter]
        if (deck.title === deckname) {
          deck.visibility = false //TODO im backend auf false setzen
          this.decks.splice(counter,1)
          localStorage.setItem('decks', JSON.stringify(this.decks));
        }
        counter++
      }
    },

    getAllDecks(): Deck[]{
      return this.decks
    },
    getTitleOfSelected(selected :Deck[]): string{
      const selectedTitles = []
      for (const deck of selected){
        selectedTitles.push(deck.title)
      }
      return selectedTitles.join(",")
    },
    getDecksTitle(): string[]{
      const TitleArr:string[] = []
      for(const deck of this.decks){
        TitleArr.push(deck.title)
      }
      return TitleArr
    },
    getFaellig(deck: Deck): number{
      return deck.stapel_id
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
      //TODO aus de mbackend auslesn welche decks visible sind und die in decks hinzufügen wenn sie noch nicht drin sind
    },
    reset_decks(): void{
      const counter: number = 0
      while (counter < this.decks.length){
        this.decks.splice(counter ,1)
        localStorage.setItem('decks',JSON.stringify(this.decks))
      }
      const deckId = this.decks.length + 1 //TODO deckID aus backend bekommen
      const authorID = 1 //TODO authorID aus backebnd bekommen
      const visible = true //TODO im backend visibility des decks setzen
      const cards_arr = [1,2,3,4,5] //TODO aus dem backend bekommen
      const newDeck: Deck = {
        title: "Strafrecht AT (Lexmea)",
        author_id: authorID,
        stapel_id: deckId,
        visibility: visible,
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
