import {defineStore} from 'pinia'
import type Card from "@/types/Card.ts";
import router from "@/router";

export const useCardStore = defineStore('card', {
  state: () => ({
    cards: [] as Card[],
    currentCardIndex: 0
  }),

  actions: {
    addCard(card:Card) {
      this.cards.push(card)
    },
    getCardIndex(){
      return this.currentCardIndex
    },
    getCardAtIndex():Card{
      return this.cards[this.currentCardIndex]
    },
    clearCards(): void{
      this.cards = []
      this.currentCardIndex = 0
    },

    resetIndex(): void{
      this.currentCardIndex = 0
    },

    //TODO: negativen Index abfangen
    indexMinusOne(): void{
      this.currentCardIndex -= 1
    },

    async updateCard(updatedCard:Card){
      const index = this.cards.findIndex(card => card.id === updatedCard.id)
      if (index !== -1){
        this.cards[index] = updatedCard
      }
    },

    getFirst(): number{
      return this.cards[0].id
    },

    //gibt die Id der nächsten Karte zurück oder -1, falls Ende der Liste erreicht
    getNextId(): number{
      let nextId: number = -1
        if (this.currentCardIndex < this.cards.length - 1) {
          nextId = this.cards[this.currentCardIndex + 1].id
        }

        this.currentCardIndex += 1
      return nextId
    },

    findCardById(id: number): Card | undefined {
      return this.cards.find((card) => card.id === id)
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

    getCards(): Card[]{
      return this.cards
    },

    loadCardPage(id: number, type: string) {
      if (type == "Definition" || type == "Problem") {
        router.push("/cards/" + id)
      } else if (type == "Schema") {
        //TODO: Aufdeck-Modus
      } else {
        console.log("invalid type")
      }
    }
  },

  getters: {
    cardsOverview: (state) => state.cards.map(({id, title, }) => {
      return {
        id,
        title
      }
    }),
  },
})
