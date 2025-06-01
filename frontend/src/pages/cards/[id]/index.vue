<template>
  <v-card
    class="mx-auto align-content-center"
    color="grey_300"
    elevation="16"
    width="600"
    min-height="400"
    @click="reveal = true"
  >
    <v-card-text>
      <p class="text-center">
        {{ testDeckName }}
      </p>
    </v-card-text>

    <p
      v-if="card"
      class="text-center text-decoration-underline"
    >
      {{ card.type }}
    </p>

    <v-card-title>
      <h3
        v-if="card"
        class="text-center"
      >
        {{ card.title }}
      </h3>
      <h3
        v-else
        class="text-center"
      >
        card title
      </h3>
    </v-card-title>

    <v-card-text
      v-if="reveal"
    >
      <p
        v-if="card"
        class="text-center text-pre-wrap"
      >
        {{ card.text }}
      </p>
      <p
        v-else
        class="text-center"
      >
        card content
      </p>
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup>
import {useCardStore} from "@/stores/card";
import {useRoute} from "vue-router";
import {ref} from 'vue'

const cardStore = useCardStore()
const route = useRoute<'/cards/[id]/'>()
const id = route.params.id
const card = cardStore.findCardById(parseInt(id))
const reveal = ref(false)
const testDeckName = "deck name" //TODO: load deck name
</script>
