import {defineStore} from 'pinia'
import type Card from "@/types/Card.ts";
import router from "@/router";

export const useCardStore = defineStore('card', {
  state: () => ({
    cards: [] as Card[],
  }),

  actions: {
    addCard(type: string, title: string, text: string, deckId: number) {
      const newId = this.cards.length + 1
      this.cards.push(
        {
          id: newId,
          type: type,
          title: title,
          text: text,
          deckId : deckId
        }
      )
    },
    findCardById(id: number): Card | undefined {
      return this.cards.find((card) => card.id === id)
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
