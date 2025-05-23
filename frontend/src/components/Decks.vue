<script setup lang="ts">
import {useDeckStore} from "@/stores/deck";
import {useUserStore} from "@/stores/users";
import {ref} from "vue";
import { computed } from 'vue';

import type Deck from "@/types/Deck";

const UserStore = useUserStore()
const DeckStore = useDeckStore()
const DialogReset = ref(false)
const DialogDeactivate = ref(false)
const SelectedCard = ref<boolean[]>([])

const decks = computed(() => DeckStore.getDecksTitle())
const faellig = computed(() => DeckStore.getDecksFaellig())
const deckID = computed(() => DeckStore.getDecksID())
const cards = computed(() => DeckStore.getCardArray())
const deckColor = computed(() => DeckStore.getDecksColor())
const colorNames = ['green', 'yellow', 'orange', 'red', 'grey'];

const dot_menu = ref<boolean[]>([])
const deckToDeactivate = ref('')
const deckToReset = ref('')

watch(
  decks,
  (newDecks) => {
    dot_menu.value = Array(newDecks.length).fill(false)
  },
  { immediate: true }
)

watch(
    decks,
    (newDecks) => {
      SelectedCard.value = Array(newDecks.length).fill(false)
    },
    { immediate: true }
)

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

function printAllActive() {
  const allDecks: Deck[]  = DeckStore.getDecks()
  for(const deck of allDecks){
    console.log(deck.title)
  }
}

function print_selected() {
  const seleted_id_arr = []
  let count: number = 0
  for(const select of SelectedCard.value){
    if(select === true){
      seleted_id_arr.push(deckID.value[count])
    }
    count ++
  }
  console.log(seleted_id_arr)
}

</script>
<template>
  <v-btn @click="print_selected">
    selected
  </v-btn>
  <v-container>
    <v-row
      v-if="UserStore.authenticated"
      no-gutters
    >
      <!--      Dashboard for authenticated User-->
      <v-col
        v-for="n in decks.length"
        :key="n"
        cols="auto"
        sm="4"
      >
        <v-sheet
          class="ma-2 pa-2"
          color="background"
        >
          <v-card
            ref="card"
            min-width="300"
            max-width="300"
            height="400"
            class="d-flex flex-column"
            variant="elevated"
            :style="{ borderColor: deckColor[n - 1], borderStyle: 'solid', borderWidth: '10px' }"
          >
            <v-card-title class="text-h5">
              {{ decks[n - 1] }}
            </v-card-title>

            <v-card-text class="text-center">
              <v-col
                class="text-h6"
                cols="auto"
              >
                {{ faellig[n - 1] }} heute fällig
                <div
                  class="progress_bar"
                  style="display:flex; width:250px; height:10px;  margin-left: -10px"
                >
                  <!--                  geht durch liste der cards für den stapel : Aufbau [Anzahl grüne Karten,Anzahl gelbe Karten,Anzahl orange Karten,Anzahl rote Karten,Anzahl graue Karten]-->
                  <div
                    v-for="(count, color_index) in cards[n-1]"
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

            <v-card-actions class="mt-auto">
              <v-row justify="space-evenly">
                <v-checkbox
                  v-model="SelectedCard[n-1]"
                  label="lernen"
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
                          @click="() => openResetDialog(decks[n - 1])"
                        >
                          Reset
                        </v-btn>
                      </v-list-item>
                      <v-list-item>
                        <v-btn
                          class="align-content-center"
                          variant="flat"
                          color="orange_darkest"
                          @click="() => openDeactivateDialog(decks[n - 1])"
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
    </v-row>
    <!--      Dashboard for unauthenticated User-->
    <v-row
      v-if="!UserStore.authenticated"
      no-gutters
    >
      <v-col
        v-for="n in decks.length"
        :key="n"
        cols="auto"
        sm="4"
      >
        <v-sheet
          class="ma-2 pa-2"
          color="background"
        >
          <v-card
            ref="card"
            min-width="300"
            max-width="300"
            height="400"
            class="d-flex flex-column"
            variant="elevated"
            :style="{ borderColor: deckColor[n - 1], borderStyle: 'solid', borderWidth: '10px' }"
          >
            <v-card-title class="text-h5">
              {{ decks[n-1] }}
            </v-card-title>

            <v-card-text class="text-center">
              <v-col
                class="text-h6"
                cols="auto"
              >
                {{ faellig[n - 1] }} heute fällig
                <div
                  class="progress_bar"
                  style="display:flex; width:250px; height:10px;  margin-left: -10px"
                >
                  <!--geht durch liste der cards für den stapel : Aufbau [Anzahl grüne Karten,Anzahl gelbe Karten,Anzahl orange Karten,Anzahl rote Karten,Anzahl graue Karten]-->
                  <div
                    v-for="(count, color_index) in cards[n-1]"
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

            <v-card-actions class="mt-auto">
              <v-row justify="space-evenly">
                <v-checkbox
                  v-model="SelectedCard[n-1]"
                  label="lernen"
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
                          @click="() => openResetDialog(decks[n - 1])"
                        >
                          Reset
                        </v-btn>
                      </v-list-item>
                      <v-list-item>
                        <v-btn
                          class="align-content-center"
                          variant="flat"
                          color="orange_darkest"
                          @click="() => openDeactivateDialog(decks[n - 1])"
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
</template>

<style scoped lang="sass">

</style>
