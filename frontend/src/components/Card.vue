<script setup lang="ts">
import {useCardStore} from "@/stores/card";
import {useDeckStore} from "@/stores/deck";
import {useRoute} from "vue-router";
import {ref} from 'vue'
import { useTheme } from 'vuetify'
import {useUserStore} from "@/stores/users";

const theme = useTheme()
const colorNames = ['green', 'yellow', 'orange', 'red', 'grey'];
const cardStore = useCardStore()
const deckStore = useDeckStore()
const userStore = useUserStore()
const router = useRouter();
const route = useRoute<'/cards/[id]/'>()
const id = route.params.id
const DialogEnd = ref(false)
const card = ref(cardStore.findCardById(parseInt(id)))
const reveal = ref(false)
const ratingArr = ref<number[]>([])
const backPossible = ref(false)
const lastRating = ref(4)

const cards = computed(() => cardStore.getCards())
const curCardIndex = computed(() => cardStore.getCardIndex())
const countGreen = computed(() => {
  let counted = 0
for(const rating of ratingArr.value){
  if (rating === 0){
    counted += 1
  }
}
return counted
})
const countYellow = computed(() => {
  let counted = 0
  for(const rating of ratingArr.value){
    if (rating === 1){
      counted += 1
    }
  }
  return counted
})
const countOrange = computed(() => {
  let counted = 0
  for(const rating of ratingArr.value){
    if (rating === 2){
      counted += 1
    }
  }
  return counted
})
const countRed = computed(() => {
  let counted = 0
  for(const rating of ratingArr.value){
    if (rating === 3){
      counted += 1
    }
  }
  return counted
})

watch(cards, newCards => {
    ratingArr.value = Array(newCards.length).fill(4);
  },
  { immediate: true }
);

watch (() => route.params.id, (newId) => {
  card.value = cardStore.findCardById(parseInt(newId))
})

function goBack(){
  DialogEnd.value = false
  backPossible.value = false
  cardStore.indexMinusOne()
  if(!userStore.authenticated) {
    const card = cardStore.getCardAtIndex()
    deckStore.undoRate(card.deckId, lastRating.value)
  }
  router.go(-1)
}

function showAnswer() {
  reveal.value = true;
}

function goHome() {
  cardStore.resetIndex()
  router.push('/')
}

function showEndDialog(){
  DialogEnd.value = true
}

function nextCard() {
  backPossible.value = true
  const nextId = cardStore.getNextId()
  if (nextId === -1) {
    showEndDialog()
    return
  }
  router.push('/cards/' + nextId)
  reveal.value = false
}

function rateCard(colorIndex: number) {
  ratingArr.value[cardStore.getCardIndex()] = colorIndex
  lastRating.value = colorIndex
  if(!userStore.authenticated) {
    const card = cardStore.getCardAtIndex()
    deckStore.rate(card.deckId, colorIndex)
  }
  nextCard()
}

const testDeckName = "Hausfriedensbruch (§ 123 StGB)" //TODO: load deck name
</script>

<template>
  <div class="progress-container d-flex flex-column align-center mb-4">
    <div class="progress-bar d-flex">
      <div
        v-for="(color_index, i) in ratingArr"
        :key="i"
        :style="{
          backgroundColor: colorNames[color_index],
          width: 100 / ratingArr.length + '%',
          border: i === curCardIndex ? '2px solid'+ theme.current.value.colors.primary : 'none'
        }"
      />
    </div>
    <div class="mt-1 text-caption text-center">
      Karte
      <span v-if="!DialogEnd">
        {{ curCardIndex + 1 }}
      </span>
      <span v-else>
        {{ cards.length }}
      </span>
      / {{ cards.length }}
    </div>
  </div>
  <v-responsive class="d-flex justify-center">
    <v-card
      class="mx-auto pa-4"
      color="grey_300"
      elevation="16"
      style="width: 100%; max-width: 600px; max-height: 80vh; overflow-y: auto;"
      :style="{borderColor: card?.color ?? 'transparent', borderStyle: 'solid', borderWidth: '10px'}"
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
        :disabled="!backPossible"
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
            @click="rateCard(3)"
          >
            <v-icon>mdi-alpha-x</v-icon>
          </v-btn>
          <v-btn
            icon
            color="orange"
            @click="rateCard(2)"
          >
            <v-icon>mdi-help</v-icon>
          </v-btn>
          <v-btn
            icon
            color="yellow"
            @click="rateCard(1)"
          >
            <v-icon>mdi-check</v-icon>
          </v-btn>
          <v-btn
            icon
            color="green"
            @click="rateCard(0)"
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
    persistent
    max-width="90vw"
  >
    <v-card
      class="mx-auto pa-4"
      style="max-width: 90vw; max-height: 90vh; overflow-y: auto;"
    >
      <v-card-title class="text-center d-flex flex-column align-center justify-center">
        <v-icon icon="mdi-party-popper" />
        <span class="mt-2">Geschafft!</span>
      </v-card-title>
      <v-card-text>
        Herzlichen Glückwunsch du hast alle Karten geschafft
        <v-row
          dense
          no-gutters
        >
          <v-col
            cols="6"
            class="pa-2"
          >
            <v-sheet
              class="pa-3 text-center rounded-lg"
              color="green_lightest"
              elevation="1"
            >
              <v-responsive
                class="text-h6 font-weight-bold"
                :style="{ color: $vuetify.theme.current.colors.green_darkest }"
              >
                {{ countGreen }}
              </v-responsive>
              <v-responsive class="text-caption">
                Einfach
              </v-responsive>
            </v-sheet>
          </v-col>
          <v-col
            cols="6"
            class="pa-2"
          >
            <v-sheet
              class="pa-3 text-center rounded-lg"
              color="yellow_lightest"
              elevation="1"
            >
              <v-responsive
                class="text-h6 font-weight-bold"
                :style="{ color: $vuetify.theme.current.colors.yellow_darkest }"
              >
                {{ countYellow }}
              </v-responsive>
              <v-responsive class="text-caption">
                Okay
              </v-responsive>
            </v-sheet>
          </v-col>
          <v-col
            cols="6"
            class="pa-2"
          >
            <v-sheet
              class="pa-3 text-center rounded-lg"
              color="orange_lightest"
              elevation="1"
            >
              <v-responsive
                class="text-h6 font-weight-bold"
                :style="{ color: $vuetify.theme.current.colors.orange_darkest }"
              >
                {{ countOrange }}
              </v-responsive>
              <v-responsive class="text-caption">
                Schwer
              </v-responsive>
            </v-sheet>
          </v-col>
          <v-col
            cols="6"
            class="pa-2"
          >
            <v-sheet
              class="pa-3 text-center rounded-lg"
              color="red_lightest"
              elevation="1"
            >
              <v-responsive
                class="text-h6 font-weight-bold"
                :style="{ color: $vuetify.theme.current.colors.red_darkest }"
              >
                {{ countRed }}
              </v-responsive>
              <v-responsive class="text-caption">
                Nicht gewusst
              </v-responsive>
            </v-sheet>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-responsive>
          <v-btn @click="goBack">
            Zurück zur letzten Karte
          </v-btn>
          <v-btn @click="goHome">
            Zurück zum Dashboard
          </v-btn>
        </v-responsive>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped lang="sass">
.progress-container
  width: 100%
  max-width: 600px
  margin: 0 auto

.progress-bar
  width: 100%
  height: 14px
  border-radius: 4px
  overflow: hidden
</style>
