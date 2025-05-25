<script setup lang="ts">
import {useDeckStore} from "@/stores/deck";
import {useUserStore} from "@/stores/users";
import {ref} from "vue";
import { computed } from 'vue';


const UserStore = useUserStore()
const DeckStore = useDeckStore()

const DialogReset = ref(false)
const DialogDeactivate = ref(false)
const DialogLearn = ref(false)

const SelectedDeck = ref<boolean[]>([])
const minOneSelected = ref(false)
const minOneTypeSelected = ref(false)
const dot_menu = ref<boolean[]>([])
const deckToDeactivate = ref('')
const deckToReset = ref('')
const definitions = ref(false)
const problems = ref(false)
const schema = ref(false)

const searchValue = ref("")

const colorNames = ['green', 'yellow', 'orange', 'red', 'grey'];

const decks = computed(() => DeckStore.getDecksTitle())
const faellig = computed(() => DeckStore.getDecksFaellig())
const deckID = computed(() => DeckStore.getDecksID())
const cards = computed(() => DeckStore.getCardArray())
const deckColor = computed(() => DeckStore.getDecksColor())

const toDisplay = computed(() => {
  const items: string[] = [];
  for (let i = 0; i < decks.value.length; i++) {
    if (decks.value[i].includes(searchValue.value)) {
      items.push(decks.value[i])
    }
  }
  return items;
});

const selectedTitles = computed(() => {
  const selected: string[] = []
  decks.value.forEach((title, index) => {
    if (SelectedDeck.value[index]) {
      selected.push(title)
    }
  })
  return selected
})

watch(
  decks,
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
    decks,
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

watch(
  [definitions,problems,schema],
  (newVal) => {
    minOneTypeSelected.value = newVal.includes(true)
  },
  { deep: true }
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

function openLearnDialog() {
  DialogLearn.value = true
}

function startLearning() {
  const selectedIDs: number[] = []
  for (let i = 0; i < SelectedDeck.value.length; i++) {
    if (SelectedDeck.value[i]) {
      selectedIDs.push(deckID.value[i])
    }
  }

  const selectedMode: string[] = []
  if (definitions.value) selectedMode.push("definitions")
  if (problems.value) selectedMode.push("problems")
  if (schema.value) selectedMode.push("schema")

  console.log("IDS: ", selectedIDs, " MODE: ", selectedMode)
}



</script>
<template>
  <Searchbar @change-value="searchValue = $event" />
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
            style="width: 300px; height: 400px"
            height="400"
            class="d-flex flex-column"
            variant="elevated"
            :style="{ borderColor: deckColor[n - 1], borderStyle: 'solid', borderWidth: '10px' }"
          >
            <v-card-title
              class="text-h5"
              style="white-space: normal;"
            >
              {{ toDisplay[n - 1] }}
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
                  v-model="SelectedDeck[n-1]"
                  label="zum lernen auswählen"
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
                          @click="() => openResetDialog(toDisplay[n - 1])"
                        >
                          Reset
                        </v-btn>
                      </v-list-item>
                      <v-list-item>
                        <v-btn
                          class="align-content-center"
                          variant="flat"
                          color="orange_darkest"
                          @click="() => openDeactivateDialog(toDisplay[n - 1])"
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
            height="400"
            class="d-flex flex-column"
            variant="elevated"
            style="width: 300px; height: 400px"
            :style="{ borderColor: deckColor[n - 1], borderStyle: 'solid', borderWidth: '10px' }"
          >
            <v-card-title
              class="text-h5"
              style="white-space: normal;"
            >
              {{ toDisplay[n-1] }}
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
                  v-model="SelectedDeck[n-1]"
                  label="zum lernen auswählen"
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
                          @click="() => openResetDialog(toDisplay[n - 1])"
                        >
                          Reset
                        </v-btn>
                      </v-list-item>
                      <v-list-item>
                        <v-btn
                          class="align-content-center"
                          variant="flat"
                          color="orange_darkest"
                          @click="() => openDeactivateDialog(toDisplay[n - 1])"
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
        {{ selectedTitles.join(', ') }}
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
      </v-list>

      <v-card-actions>
        <v-btn
          color="primary"
          :disabled="!minOneTypeSelected"
          @click="startLearning"
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
</template>

<style scoped lang="sass">
.learn-btn
  position: fixed
  bottom: 20px
  right: 20px
  z-index: 100
</style>
