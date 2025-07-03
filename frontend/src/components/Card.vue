<script setup lang="ts">
import {useCardStore} from "@/stores/card";
import {useDeckStore} from "@/stores/deck";
import {useRoute} from "vue-router";
import {ref} from 'vue'
import {useUserStore} from "@/stores/users";


const colorNames = ['green', 'yellow', 'orange', 'red', 'grey'];
const colorsChip = ['#92C1A5', '#FBDC5C', '#FFB571', '#FFAA9F', '#CBD0D7'];

const cardStore = useCardStore()
const deckStore = useDeckStore()
const userStore = useUserStore()
const router = useRouter();
const route = useRoute<'/cards/[id]/'>()
const id = route.params.id
const DialogEnd = ref(false)
const card = ref(cardStore.findCardById(parseInt(id)))

interface content {
  data: string
  index: number
  spacing: number
}

const contentList: Ref<content[]> = ref(getContentList(getHeadlines(), 0, 0))
const revealedText: Ref<content[]> = ref([])

if (card.value) {
  if (card.value.type == "Aufdeckkarte") {
    console.log(JSON.parse(card.value.text))
  }
}

function getHeadlines() {
  if (card.value && card.value.type == "Aufdeckkarte") {
    const root = JSON.parse(card.value.text)
    const content = root.children
    return content[0].children
  }
}



/*
function test() {
  const content = getContent()
  for (const object of content) {
    contentList.push(object.data)

    for (const object2 of object.children) {
      contentList.push("\t" + object2.data)

      for (const object3 of object2.children) {
        contentList.push("\t\t" + object3.data)
      }
    }
  }
  console.log(contentList)
}
 */

function getContentList(content, i: number, depth: number): content[] {
  if (content) {
    let list: content[] = []

    for (const item of content) {
      list.push({data: item.data, index: i, spacing: depth}) //doesnt work
      i++

      if (item.children) {
        list = list.concat(getContentList(item.children, i, depth + 1))
      }
    }
    return list
  }
  return []
}

const reveal = ref(false)
const ratingArr = ref<number[]>([])
const backPossible = ref(false)
const lastRating = ref(4)
const ratingLabels = ['Einfach', 'Okay', 'Schwer', 'Nicht gewusst', 'Unbewertet']

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
  contentList.value = getContentList(getHeadlines(), 0, 0)
  if (backPossible.value) {
    revealedText.value = []
  } else {
    revealedText.value = contentList.value
  }

})

function getLastRatingText(): string {
    const curCard = card.value
    if (curCard) {
      return ratingLabels[curCard.lastRating]
    } else {
      return ratingLabels[4]
    }

}

function getLastColor():string{
  const curCard = card.value
  if(curCard){
    return colorsChip[curCard.lastRating]
  }
  else{
    return colorsChip[4]
  }
}

function goBack(){
  DialogEnd.value = false
  backPossible.value = false
  cardStore.indexMinusOne()
  const prevId = cardStore.getCardAtIndex().id;
  router.replace(`/cards/${prevId}`);
}

function showAnswer() {
  if (card.value && card.value.type == "Aufdeckkarte") {
    const linesRevealed = revealedText.value.length

    /*
    if (contentList.value.length == 0) {
      contentList.value = getContentList(getHeadlines(), 0, 0)
    }
     */
    if (linesRevealed >= contentList.value.length) {
      reveal.value = true;
    } else {
      revealedText.value.push(contentList.value[linesRevealed])
    }

  } else {
    reveal.value = true;
  }
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
  revealedText.value = []
  contentList.value = []
}

function rateCard(colorIndex: number) {
  ratingArr.value[cardStore.getCardIndex()] = colorIndex
  lastRating.value = colorIndex
  if(!userStore.authenticated) {
    const card = cardStore.getCardAtIndex()
    deckStore.rate(card.id,card.deckID,colorIndex)
  }
  nextCard()
}

const testDeckName = "Hausfriedensbruch (§ 123 StGB)" //TODO: load deck name
//TODO: fix formatting on problem/definition
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
          border: i === curCardIndex ? '2px solid black' : 'none'
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
  <div class="card-button-wrap">
    <v-responsive class="card">
      <v-card
        class="mx-auto pa-4"
        color="grey_300"
        elevation="16"
        style="width: 100%; max-width: 600px; overflow-y: auto;"
        :style="{borderColor: card?.color ?? 'transparent', borderStyle: 'solid', borderWidth: '10px'}"
        @click="showAnswer()"
      >
        <v-card-text>
          <p class="text-center">
            {{ testDeckName }}
          </p>
        </v-card-text>


        <v-row
          v-if="card"
          class="mb-2"
          align="center"
          justify="center"
          no-gutters
        >
          <v-col
            cols="12"
            class="text-center"
          >
            <span class="text-decoration-underline">{{ card.type }}</span>
          </v-col>
        </v-row>

        <v-row
          v-if="card && reveal"
          class="mb-2"
          justify="center"
        >
          <v-col cols="auto">
            <v-chip
              v-if="reveal"
              :style="{ backgroundColor: getLastColor(), color: 'black' }"
              class="ma-2"
              label
            >
              Letzte Bewertung: {{ getLastRatingText() }}
            </v-chip>
          </v-col>
        </v-row>

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

        <template
          v-if="card"
        >
          <v-card-text
            v-if="card.type == 'Aufdeckkarte'"
            class="text-pre-wrap"
          >
            <div
              v-for="item in revealedText"
              :key="item.data"
            >
              <h3> {{ "\t".repeat(item.spacing) + item.data }} </h3>
            </div>
          </v-card-text>
          <p
            v-if="reveal && card.type != 'Aufdeckkarte'"
            class="text-center text-justify"
          >
            {{ card.text }}
          </p>
        </template>
        <template v-else>
          card text
        </template>
      </v-card>
    </v-responsive>

    <v-container
      class="button px-4"
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
          @click="goBack()"
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
  </div>

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
          <v-btn @click="goBack()">
            Zurück zur letzten Karte
          </v-btn>
          <v-btn @click="goHome()">
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

.card-button-wrap
  display: flex
  flex-direction: column
  height: 80vh

.card
  flex: 1 1 auto
  overflow-y: auto

.button
  flex: 0 0 auto
  padding-top: 12px
</style>
