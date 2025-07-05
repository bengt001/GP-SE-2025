<script setup lang="ts">
import {useDeckStore} from "@/stores/deck";
import {useUserStore} from "@/stores/users";
import {ref} from "vue";
import { computed } from 'vue';
import type Deck from "@/types/Deck"
import {useCardStore} from "@/stores/card";
import router from "@/router";
import type Card from "@/types/Card";


const UserStore = useUserStore()
const DeckStore = useDeckStore()
const CardStore = useCardStore()

const DialogReset = ref(false)
const DialogDeactivate = ref(false)
const DialogLearn = ref(false)
const learnFinished = ref(false)


const SelectedDeck = ref<boolean[]>([])
const minOneSelected = ref(false)
const dot_menu = ref<boolean[]>([])
const deckToDeactivate = ref('')
const deckToReset = ref('')
const definitions = ref(false)
const problems = ref(false)
const schema = ref(false)
const numberOfCards = ref(0)

const menu = ref(false)
const lexmea = ref(true)
const own = ref(true)
const broad = ref(true)

const searchValue = ref("")

const colorNames = ['green', 'yellow', 'orange', 'red', 'grey'];

const allDecks = computed(() => DeckStore.getAllDecks())

const toDisplay = computed(() => {
  const items: Deck[] = [];
  for (let i = 0; i < allDecks.value.length; i++) {
    if (allDecks.value[i].title.includes(searchValue.value)) {
      if(lexmea.value && allDecks.value[i].title.includes("Lexmea")){
        items.push(allDecks.value[i])
      }
      else if(own.value && allDecks.value[i].title.includes("Eigener Stapel")){
        items.push(allDecks.value[i])
      }
      else if(broad.value && allDecks.value[i].title.includes("Broadcast")){
        items.push(allDecks.value[i])
      }
    }
  }
  return items;
});

const selectedDecksTitle = computed(() => {
  const selected: Deck[] = []
  allDecks.value.forEach((deck, index) => {
    if (SelectedDeck.value[index]) {
      selected.push(deck)
    }
  })
  return selected
})

onMounted(() => {
  DeckStore.loadFromLocalStorage()
})

watch(() => DeckStore.decks, (newVal) => {
  localStorage.setItem('decks', JSON.stringify(newVal))
}, { deep: true })


watch(
  allDecks,
  (newDecks) => {
    dot_menu.value = Array(newDecks.length).fill(false)
  },
  { immediate: true }
)

watch(
  SelectedDeck,
  (newVal) => {
    minOneSelected.value = newVal.includes(true)
  },
  { deep: true }
)


watch(
    allDecks,
    (newDecks) => {
      SelectedDeck.value = Array(newDecks.length).fill(false)
    },
    { immediate: true }
)

watch(schema, (newVal) => {
  if (newVal) {
    definitions.value = false
    problems.value = false
  }
})

watch([definitions, problems], ([newDefinitions, newProblems]) => {
  if (newDefinitions || newProblems) {
    schema.value = false
  }
})

watch([definitions, problems, schema], () => {
    const selectedTypes = getSelectedTypes()
    const newMax = getCardCount(selectedTypes)
    if (numberOfCards.value > newMax) {
      numberOfCards.value = newMax
    }
  })

const anyTypeSelected = computed(() =>
  definitions.value || problems.value || schema.value
)

const canLearn = computed(() =>
  anyTypeSelected.value && numberOfCards.value > 0
)

function getSelectedTypes(): string[] {
  const arr: string[] = []
  if (definitions.value) arr.push("Definitionen")
  if (problems.value)    arr.push("Probleme")
  if (schema.value)      arr.push("Schema")
  return arr
}

function openResetDialog(deckTitle: string) {
  deckToReset.value = deckTitle
  DialogReset.value = true
}

function resetCards() {
  DeckStore.resetCards(deckToReset.value)
  DialogReset.value = false
}

function openDeactivateDialog(deckTitle: string) {
  deckToDeactivate.value = deckTitle
  DialogDeactivate.value = true
}

function deactivateCards() {
  DeckStore.deactivateDeck(deckToDeactivate.value)
  DialogDeactivate.value=false
}

function openLearnDialog() {


  DialogLearn.value = true
}

function plus5() {
  numberOfCards.value += 5
  const max = getCardCount(getSelectedTypes())
  if (numberOfCards.value > max) {
    numberOfCards.value = max
  }
}

function minus5() {
  if (numberOfCards.value <= 5)
  {
    return
  }
  numberOfCards.value -= 5
}
function plus() {
  numberOfCards.value += 1
  const max = getCardCount(getSelectedTypes())
  if (numberOfCards.value > max) {
    numberOfCards.value = max
  }
}

function minus() {
  if (numberOfCards.value <= 1)
  {
    return
  }
  numberOfCards.value -= 1
}

function getCardCount(selectedCardTypes:string[]): number{

  let cardCount: number = 0

  for (let i = 0; i < SelectedDeck.value.length; i++) {
    if (SelectedDeck.value[i]) {
      for(const type of selectedCardTypes){
        if(type == "Probleme"){
          cardCount += allDecks.value[i].problems.length
        }
        else if(type == "Definitionen"){
          cardCount += allDecks.value[i].definitions.length
        }
        else if(type == "Schema"){
          cardCount += allDecks.value[i].schemas.length
        }
      }
    }
  }
  return cardCount
}

//Methode die genuttzt wird um enene Card array so zu sortieren, dass Alle Karten von ausgewählten decks so soritert sind
// , dass hinten unbewetete Karten sind und nach vorne von schlecht bis gut sortiert sind
function sortCards(cardsToSort:Card[]):Card[]{
  const green:Card[] = []
  const yellow:Card[] = []
  const orange:Card[] = []
  const red:Card[] = []
  const grey:Card[] = []
  for(const card of cardsToSort){
    switch (card.lastRating){
      case 0:
        green.push(card)
        break
      case 1:
        yellow.push(card)
        break
      case 2:
        orange.push(card)
        break
      case 3:
        red.push(card)
        break
      case 4:
        grey.push(card)
        break
    }
  }
  return green.concat(yellow,orange,red,grey)
}

async function startLearning() {
  CardStore.clearCards()
  let Cards:Card[] = []

  const selectedMode: string[] = []
  if (definitions.value) selectedMode.push("Definitionen")
  if (problems.value) selectedMode.push("Probleme")
  if (schema.value) selectedMode.push("Aufdeckkarte")

  for (let i = 0; i < SelectedDeck.value.length; i++) {
    if (!SelectedDeck.value[i]) {
      continue
    }
    for(const mode of selectedMode){
      if(mode == "Definitionen"){
        Cards = Cards.concat(allDecks.value[i].definitions)
      }
      else if(mode == "Probleme"){
        Cards = Cards.concat(allDecks.value[i].problems)
      }
      else if(mode == "Aufdeckkarte"){
        Cards = Cards.concat(allDecks.value[i].schemas)
      }
    }
  }


  if(!UserStore.authenticated) {
    Cards = sortCards(Cards).slice(-numberOfCards.value).reverse()
  }
  else{
    const deckIds:number[] = []
    for (let i = 0; i < SelectedDeck.value.length; i++) {
      if (!SelectedDeck.value[i]) {
        continue
      }
        for(const curId of allDecks.value[i].stapel_id){
        deckIds.push(curId)
      }
    }

    Cards = await DeckStore.getCardsToLearn(deckIds,numberOfCards.value,selectedMode,Cards)

  }

  if(Cards.length <= 0){
    learnFinished.value = true
    return
  }

  for(const card of Cards){
    CardStore.addCard(card)
  }

  router.push("/cards/" + CardStore.getFirst())
}



</script>
<template>
  <Searchbar @change-value="searchValue=$event" />
  <v-menu
    v-model="menu"
    :close-on-content-click="false"
  >
    <template #activator="{ props }">
      <v-btn
        class="v-btn--flat filter-btn"
        v-bind="props"
        icon="mdi-filter"
        color="primary"
      />
    </template>
    <v-card min-width="300">
      <v-list>
        <v-list-item>
          <v-switch
            v-model="lexmea"
            color="primary"
            label="Lexmea"
            hide-details
          />
        </v-list-item>
        <v-list-item>
          <v-switch
            v-model="broad"
            color="primary"
            label="Broadcast"
            hide-details
          />
        </v-list-item>
        <v-list-item v-if="UserStore.authenticated">
          <v-switch
            v-model="own"
            label="Eigene Stapel"
            color="primary"
            hide-details
          />
        </v-list-item>
      </v-list>
    </v-card>
  </v-menu>

  <v-container class="overflow-hidden">
    <v-row
      v-if="UserStore.authenticated"
    >
      <!--      Dashboard for authenticated User-->
      <v-col
        v-for="n in toDisplay.length"
        :key="n"
        cols="auto"
      >
        <v-sheet
          class="ma-2"
          color="background"
        >
          <v-card
            ref="card"
            style="width: 300px; height: 300px"
            class="d-flex flex-column"
            variant="elevated"
            :style="{ borderColor: toDisplay[n - 1].color, borderStyle: 'solid', borderWidth: '10px' }"
          >
            <v-card-title
              class="text-h5 no-word-break"
              style="white-space: normal;"
            >
              {{ toDisplay[n - 1].title }}
            </v-card-title>
            <v-spacer />
            <v-card-text class="text-center">
              <v-col
                class="text-h6"
                cols="auto"
              >
                {{ DeckStore.getFaellig(toDisplay[n - 1]) }} heute fällig
                <div
                  class="progress_bar"
                  style="display:flex; width:250px; height:10px;  margin-left: -10px"
                >
                  <!--  geht durch liste der cards für den stapel : Aufbau [Anzahl grüne Karten,Anzahl gelbe Karten,Anzahl orange Karten,Anzahl rote Karten,Anzahl graue Karten]-->
                  <div
                    v-for="(count, color_index) in DeckStore.getDeckRating(toDisplay[n-1])"
                    :key="color_index"
                    :style="{
                      backgroundColor: colorNames[color_index],
                      flexGrow: count,
                      height: '100%'
                    }"
                  />
                </div>
              </v-col>
            </v-card-text>
            <v-spacer />
            <v-card-actions class="mt-auto">
              <v-row justify="space-evenly">
                <v-checkbox
                  v-model="SelectedDeck[n-1]"
                  label="Zum Lernen auswählen"
                  color="lexmea_blue_200"
                />

                <v-menu
                  v-model="dot_menu[n - 1]"
                  :close-on-content-click="true"
                >
                  <template #activator="{ props }">
                    <v-btn
                      v-bind="props"
                      icon="mdi-dots-horizontal"
                      color="primary"
                    />
                  </template>
                  <v-card min-width="200">
                    <v-list>
                      <v-list-item>
                        <v-btn
                          class="align-content-center"
                          variant="flat"
                          color="red_darkest"
                          @click="() => openResetDialog(toDisplay[n - 1].title)"
                        >
                          Reset
                        </v-btn>
                      </v-list-item>
                      <v-list-item>
                        <v-btn
                          class="align-content-center"
                          variant="flat"
                          color="orange_darkest"
                          @click="() => openDeactivateDialog(toDisplay[n - 1].title)"
                        >
                          Deaktivieren
                        </v-btn>
                      </v-list-item>
                    </v-list>
                  </v-card>
                </v-menu>
              </v-row>
            </v-card-actions>
          </v-card>
        </v-sheet>
      </v-col>
      <v-col
        v-for="i in DeckStore.getloadingDecks()"
        :key="i"
        cols="auto"
      >
        <v-skeleton-loader
          type="card"
          style="width:300px; height:300px; margin:8px"
        />
      </v-col>
    </v-row>
    <!--      Dashboard for unauthenticated User-->
    <v-row
      v-if="!UserStore.authenticated"
    >
      <v-col
        v-for="n in toDisplay.length"
        :key="n"
        cols="auto"
      >
        <v-sheet
          class="ma-2"
          color="background"
        >
          <v-card
            ref="card"
            class="d-flex flex-column"
            variant="elevated"
            style="width: 300px; height: 300px"
            :style="{ borderColor: toDisplay[n - 1].color, borderStyle: 'solid', borderWidth: '10px' }"
          >
            <v-card-title
              class="text-h5 no-word-break"
              style="white-space: normal;"
            >
              {{ toDisplay[n-1].title }}
            </v-card-title>
            <v-spacer />
            <v-card-text class="text-center">
              <v-col
                class="text-h6"
                cols="auto"
              >
                {{ DeckStore.getFaellig(toDisplay[n - 1]) }} heute fällig
                <div
                  class="progress_bar"
                  style="display:flex; width:250px; height:10px;  margin-left: -10px"
                >
                  <!--geht durch liste der cards für den stapel : Aufbau [Anzahl grüne Karten,Anzahl gelbe Karten,Anzahl orange Karten,Anzahl rote Karten,Anzahl graue Karten]-->
                  <div
                    v-for="(count, color_index) in DeckStore.getDeckRating(toDisplay[n-1])"
                    :key="color_index"
                    :style="{
                      backgroundColor: colorNames[color_index],
                      flexGrow: count,
                      height: '100%'
                    }"
                  />
                </div>
              </v-col>
            </v-card-text>
            <v-spacer />
            <v-card-actions class="mt-auto">
              <v-row justify="space-evenly">
                <v-checkbox
                  v-model="SelectedDeck[n-1]"
                  label="Zum Lernen auswählen"
                  color="lexmea_blue_200"
                />
                <v-menu
                  v-model="dot_menu[n - 1]"
                  :close-on-content-click="false"
                >
                  <template #activator="{ props }">
                    <v-btn
                      v-bind="props"
                      icon="mdi-dots-horizontal"
                      color="primary"
                    />
                  </template>
                  <v-card min-width="200">
                    <v-list>
                      <v-list-item>
                        <v-btn
                          class="align-content-center"
                          variant="flat"
                          color="red_darkest"
                          @click="() => openResetDialog(toDisplay[n - 1].title)"
                        >
                          Reset
                        </v-btn>
                      </v-list-item>
                      <v-list-item>
                        <v-btn
                          class="align-content-center"
                          variant="flat"
                          color="orange_darkest"
                          @click="() => openDeactivateDialog(toDisplay[n - 1].title)"
                        >
                          Deaktivieren
                        </v-btn>
                      </v-list-item>
                    </v-list>
                  </v-card>
                </v-menu>
              </v-row>
            </v-card-actions>
          </v-card>
        </v-sheet>
      </v-col>

      <v-col
        v-for="i in DeckStore.getloadingDecks()"
        :key="i"
        cols="auto"
      >
        <v-skeleton-loader
          type="card"
          style="width:300px; height:300px; margin:8px"
        />
      </v-col>
    </v-row>
  </v-container>

  <v-dialog
    v-model="DialogDeactivate"
    max-width="344"
  >
    <v-card>
      <v-card-title>
        Karten deaktivieren
      </v-card-title>
      <v-card-text>
        Bist du sicher, dass du diesen Stapel deaktivieren möchtest?
      </v-card-text>
      <v-card-actions>
        <v-btn @click="deactivateCards">
          Ja, deaktivieren
        </v-btn>
        <v-btn @click="DialogDeactivate=false">
          Abbrechen
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog
    v-model="DialogReset"
    max-width="344"
  >
    <v-card>
      <v-card-title>
        Karten resetten
      </v-card-title>
      <v-card-text>
        Bist du sicher, dass du diesen Stapel resetten möchtest?
      </v-card-text>
      <v-card-actions>
        <v-btn @click="resetCards()">
          Ja, resetten
        </v-btn>
        <v-btn @click="DialogReset=false">
          Abbrechen
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog
    v-model="DialogLearn"
    max-width="600"
  >
    <v-card>
      <v-card-title>
        Lernmodus auswahl
      </v-card-title>
      <v-card-text>
        Stapel die du zum lernen ausgewählt hast:
      </v-card-text>
      <v-card-text>
        {{ DeckStore.getTitleOfSelected(selectedDecksTitle) }}
      </v-card-text>
      <v-list>
        <v-list-item>
          <v-switch
            v-model="definitions"
            color="primary"
            label="Definitionen"
            hide-details
          />
        </v-list-item>
        <v-list-item>
          <v-switch
            v-model="problems"
            color="primary"
            label="Probleme"
            hide-details
          />
        </v-list-item>
        <v-list-item>
          <v-switch
            v-model="schema"
            color="primary"
            label="Schema"
            hide-details
          />
        </v-list-item>
        <v-list-item>Zum lernen Ausgewählte Karten:</v-list-item>
        <v-list-item>
          <v-row
            class="align-center"
            no-gutters
          >
            <v-col
              cols="auto"
              class="px-1"
            >
              <v-btn
                color="primary"
                size="small"
                @click="minus5"
              >
                <v-icon size="16">
                  mdi-minus
                </v-icon>
                <span class="ml-1">5</span>
              </v-btn>
            </v-col>
            <v-col
              cols="auto"
              class="px-1"
            >
              <v-btn
                color="primary"
                size="small"
                @click="minus"
              >
                <v-icon size="16">
                  mdi-minus
                </v-icon>
                <span class="ml-1">1</span>
              </v-btn>
            </v-col>
            <v-col
              cols="auto"
              class="px-2"
            >
              <div class="text-h6">
                {{ numberOfCards }}
              </div>
            </v-col>
            <v-col
              cols="auto"
              class="px-1"
            >
              <v-btn
                color="primary"
                size="small"
                @click="plus"
              >
                <v-icon size="16">
                  mdi-plus
                </v-icon>
                <span class="ml-1">1</span>
              </v-btn>
            </v-col>
            <v-col
              cols="auto"
              class="px-1"
            >
              <v-btn
                color="primary"
                size="small"
                @click="plus5"
              >
                <v-icon size="16">
                  mdi-plus
                </v-icon>
                <span class="ml-1">5</span>
              </v-btn>
            </v-col>
            <v-spacer />
            <v-col
              cols="auto"
              class="px-2"
            >
              <div class="text-body-2">
                Dies entspricht ca. {{ (numberOfCards * 0.7).toFixed(1) }} Minuten Lernzeit
              </div>
            </v-col>
          </v-row>
        </v-list-item>
      </v-list>

      <v-card-actions>
        <v-btn
          color="primary"
          :disabled="!canLearn"
          @click="startLearning()"
        >
          <v-icon start>
            mdi-school
          </v-icon>Lernen starten
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-btn
    v-if="minOneSelected"
    color="primary"
    class="learn-btn"
    min-height="50px"
    @click="openLearnDialog"
  >
    <v-icon start>
      mdi-school
    </v-icon>
    lernen
  </v-btn>

  <v-snackbar
    v-model="learnFinished"
    :timeout="2000"
    class="elevation-24"
    color="error"
  >
    Alle Karten für heute gelernt
  </v-snackbar>
</template>

<style scoped lang="sass">
.learn-btn
  position: fixed
  bottom: 20px
  right: 20px
  z-index: 100

.filter-btn
  position: fixed
  bottom: 20px
  left: 20px
  z-index: 100

.no-word-break
  word-break: keep-all
  overflow-wrap: normal
</style>
