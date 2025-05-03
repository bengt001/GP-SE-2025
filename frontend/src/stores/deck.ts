import {defineStore} from "pinia";
import type Deck from "@/types/Deck";

export const useDeckStore = defineStore('decks', {
  state: () => ({
    decks: [] as Deck[],
  }),
})
