import {defineStore} from 'pinia'
import type Card from "@/types/Card.ts";
import router from "@/router";

export const useCardStore = defineStore('card', {
  state: () => ({
    cards: [] as Card[],
  }),

  actions: {
    addCard(type: string, title: string, text: string, deckId: number,cardId: number) {
      this.cards.push(
        {
          id: cardId,
          type: type,
          title: title,
          text: text,
          deckId : deckId
        }
      )
    },
    clearCards(): void{
      this.cards = []
    },

    getFirst(): number{
      return this.cards[0].id
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

    getCard(deckIds: number[], types: string[]): Card | undefined {
      for (const card of this.cards) {
        if (deckIds.includes(card.deckId) && types.includes(card.type)) {
          console.log("found card: " + card.title)
          return card
        }
      }
      console.log("no card found")
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
