import {defineStore} from 'pinia'
import type Deck from "@/types/Deck";

export const useDeckStore = defineStore('decks', {
  state: () => ({
    decks: JSON.parse(localStorage.getItem('decks') || '[]') as Deck[],
  }),

  actions: {
    addDeck(deckname: string): void{
      const exists = this.decks.some(deck => deck.title === deckname);
      if (exists){
        return
      }
      const deckId = this.decks.length + 1 //TODO deckID aus backend bekommen
      const authorID = 1 //TODO authorID aus backebnd bekommen
      const visible = true //TODO im backend visibility des decks setzen
      const newDeck: Deck = {
        title: deckname,
        author_id: authorID,
        stapel_id: deckId,
        visibility: visible
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

    getDecks(): Deck[]{
      return this.decks
    },
    getDecksTitle(): string[]{
      const TitleArr:string[] = []
      for(const deck of this.decks){
        TitleArr.push(deck.title)
      }
      return TitleArr
    },
    getDecksFaellig(): number[]{
      const FaelligArr:number[] = []
      for(const deck of this.decks){
        FaelligArr.push(deck.stapel_id) //TODO auslesen aus backend wieviele Karten fällig sind
      }
      return FaelligArr
    },
    get_my_active_decks(): void{
      //TODO aus de mbackend auslesn welche decks visible sind und die in decks hinzufügen wenn sie noch nicht drin sind
    },
    reset_decks(): void{
      let counter: number = 0
      while (counter < this.decks.length){
        this.decks.splice(counter ,1)
        localStorage.setItem('decks',JSON.stringify(this.decks))
      }
      counter ++
      const deckId = this.decks.length + 1 //TODO deckID aus backend bekommen
      const authorID = 1 //TODO authorID aus backebnd bekommen
      const visible = true //TODO im backend visibility des decks setzen
      const newDeck: Deck = {
        title: "StrafrechtAT",
        author_id: authorID,
        stapel_id: deckId,
        visibility: visible
      }
      this.decks.push(newDeck)
      localStorage.setItem('decks', JSON.stringify(this.decks));
    }

  },
})
