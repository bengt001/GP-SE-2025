import {defineStore} from 'pinia'

import type Card from "@/types/Card.ts";

export const useCardStore = defineStore('card', {

  state: () => ({
    cards: [] as Card[],
  }),

  actions: {
    addCard(type: string, title: string, text: string) {
      const newId = this.cards.length + 1
      this.cards.push(
        {
          id: newId,
          type: type,
          title: title,
          text: text
        }
      )
    },

    findCardById(id: number): Card | undefined {
      return this.cards.find((card) => card.id === id)
    },
    loadCards(ids: number[],modes: string[]){
      for (const id of ids){
        console.log(id,modes)  //TODO passende Karten aus dem backend laden
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
