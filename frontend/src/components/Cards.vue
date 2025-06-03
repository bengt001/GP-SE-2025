<script setup lang="ts">
  import {useCardStore} from "@/stores/card";
  //import router from "@/router";
  const cardStore = useCardStore();

  //test card
  const type = "Definition"
  const title = "Kausal"
  const text = "Jede Bedingung, die nicht hinweggedacht werden kann, ohne dass der Erfolg in seiner konkreten " +
    "Gestalt entfiele (Äquivalenztheorie / conditio-sine-qua-non-Formel).\n\nErgänzungssatz für Fälle alternativer " +
    "Kausalität: Von mehreren Bedingungen, die alternativ, nicht aber kumulativ hinweggedacht werden können, ist jede " +
    "kausal."

  function testCard() {
    cardStore.addCard(type, title, text, 1)
    cardStore.addCard("", "card2", "", 0);
  }

  function filterTest() {
    const decks = [1]
    const types = ["Definition", "Problem"]
    const card = cardStore.getCard(decks, types)
    if (card) {
      cardStore.loadCardPage(card.id, card.type)
      //router.push("/cards/" + card.id)
    }
  }
</script>

<template>
  <button @click="testCard()">
    test card
  </button>
  <ul>
    <li
      v-for="card in cardStore.cardsOverview"
      :key="card.id"
    >
      <router-link :to="'/cards/' + card.id">
        {{ card.title }}
      </router-link>
    </li>
  </ul>

  <button @click="filterTest()">
    filter test
  </button>
</template>

<style scoped lang="sass">

</style>
