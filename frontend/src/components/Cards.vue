<script setup lang="ts">
  import {useCardStore} from "@/stores/card";
  const cardStore = useCardStore();

  //test card
  const type = "Definition"
  const title = "Kausal"
  const text = "Jede Bedingung, die nicht hinweggedacht werden kann, ohne dass der Erfolg in seiner konkreten " +
    "Gestalt entfiele (Äquivalenztheorie / conditio-sine-qua-non-Formel).\n\nErgänzungssatz für Fälle alternativer " +
    "Kausalität: Von mehreren Bedingungen, die alternativ, nicht aber kumulativ hinweggedacht werden können, ist jede " +
    "kausal."

</script>

<template>
  <button @click="cardStore.addCard(type, title, text)">test card</button>
  <ul>
    <li v-for="card in cardStore.cardsOverview" :key="card.id">
      <router-link :to="'/cards/' + card.id">
        {{ card.title }}
      </router-link>
    </li>
  </ul>
</template>

<style scoped lang="sass">

</style>
