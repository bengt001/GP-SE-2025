<script setup lang="ts">
import {useCardStore} from "@/stores/card";
import {useRoute} from "vue-router";
import {ref} from 'vue'

const DialogEnd = ref(false)

const cardStore = useCardStore()
const router = useRouter();
const route = useRoute<'/cards/[id]/'>()
const id = route.params.id
const card = ref(cardStore.findCardById(parseInt(id)))
const reveal = ref(false)
watch (() => route.params.id, (newId) => {
  card.value = cardStore.findCardById(parseInt(newId))
})

//Buttons letzte Karte, Antwort Zeigen, Home
const goBack = () => {
  cardStore.indexMinusOne()
  router.go(-1)
  // Hier kannst du router.push oder andere Logik einfügen
}

const showAnswer = () => {
  reveal.value = true;
}

const goHome = () => {
  cardStore.resetIndex()
  router.push('/')
}

function showEndDialog(){
  DialogEnd.value = true
}

function nextCard() {
  const nextId = cardStore.getNextId()
  if(nextId === -1){
    showEndDialog()
    return;
  }
  router.push('/cards/' + cardStore.getNextId())
  reveal.value = false
}

const testDeckName = "Hausfriedensbruch (§ 123 StGB)" //TODO: load deck name
</script>

<template>
  <v-responsive class="d-flex justify-center">
    <v-card
      class="mx-auto pa-4"
      color="grey_300"
      elevation="16"
      style="width: 100%; max-width: 600px; max-height: 80vh; overflow-y: auto;"
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
          class="text-center text-wrap"
          style="word-break: break-word;"
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
  </v-responsive>


  <v-container
    class="mt-6 px-4"
    style="max-width: 600px;"
    fluid
  >
    <div
      class="d-flex justify-between align-center flex-wrap"
      style="gap: 12px;"
    >
      <v-btn
        icon
        style="flex: 0 0 auto;"
        @click="goBack"
      >
        <v-icon>mdi-arrow-left</v-icon>
      </v-btn>

      <div
        style="flex: 1 1 auto;"
        class="text-center"
      >
        <v-btn
          v-if="!reveal"
          color="primary"
          class="mx-auto"
          @click="showAnswer"
        >
          Antwort anzeigen
        </v-btn>

        <div
          v-else
          class="d-flex justify-center flex-wrap"
          style="gap: 8px;"
        >
          <v-btn
            icon
            color="red"
            @click="nextCard()"
          >
            <v-icon>mdi-alpha-x</v-icon>
          </v-btn>
          <v-btn
            icon
            color="orange"
            @click="nextCard()"
          >
            <v-icon>mdi-help</v-icon>
          </v-btn>
          <v-btn
            icon
            color="yellow"
            @click="nextCard()"
          >
            <v-icon>mdi-check</v-icon>
          </v-btn>
          <v-btn
            icon
            color="green"
            @click="nextCard()"
          >
            <v-icon>mdi-check-all</v-icon>
          </v-btn>
        </div>
      </div>

      <v-btn
        icon
        style="flex: 0 0 auto;"
        @click="goHome"
      >
        <v-icon>mdi-home</v-icon>
      </v-btn>
    </div>
  </v-container>

  <v-dialog
    v-model="DialogEnd"
    max-width="344"
  >
    <v-card>
      <v-card-title class="text-center d-flex flex-column align-center justify-center">
        <v-icon icon="mdi-party-popper" />
        <span class="mt-2">Geschafft!</span>
      </v-card-title>
      <v-card-text>
        Herzlichen Glückwunsch du hast alle Karten geschafft
      </v-card-text>
      <v-card-actions>
        <v-btn @click="goHome">
          Zurück zum Dashboard
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped lang="sass">




</style>
