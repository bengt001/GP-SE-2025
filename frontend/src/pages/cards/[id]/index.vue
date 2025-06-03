
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
        class="text-center text-justify"
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

  <v-container
    class="mt-6"
    style="max-width: 600px;"
  >
    <v-row
      align="center"
      justify="space-between"
    >
      <v-col
        cols="2"
        class="text-left"
      >
        <v-btn
          icon
          @click="goBack"
        >
          <v-icon>mdi-arrow-left</v-icon>
        </v-btn>
      </v-col>

      <v-col
        cols="8"
        class="text-center"
      >
        <v-btn
          color="primary"
          @click="showAnswer"
        >
          Antwort anzeigen
        </v-btn>
      </v-col>

      <v-col
        cols="2"
        class="text-right"
      >
        <v-btn
          icon
          @click="goHome"
        >
          <v-icon>mdi-home</v-icon>
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import {useCardStore} from "@/stores/card";
import {useRoute} from "vue-router";
import {ref} from 'vue'

const cardStore = useCardStore()
const router = useRouter();
const route = useRoute<'/cards/[id]/'>()
const id = route.params.id
const card = cardStore.findCardById(parseInt(id))
const reveal = ref(false)

//Buttons letzte Karte, Antwort Zeigen, Home
const goBack = () => {
  console.log("Zurück geklickt");
  // Hier kannst du router.push oder andere Logik einfügen
}

const showAnswer = () => {
  reveal.value = true;
}

const goHome = () => {
  router.push('/')
}

const testDeckName = "Hausfriedensbruch (§ 123 StGB)" //TODO: load deck name
</script>
