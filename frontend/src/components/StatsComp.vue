<script setup lang="ts">
import {useDeckStore} from "@/stores/deck";
import {useUserStore} from "@/stores/users";
import {ref} from "vue";
import { computed } from 'vue';
import type Deck from "@/types/Deck"
import ColoredSparkline from "@/components/ColoredSparkline.vue";

const UserStore = useUserStore()
const DeckStore = useDeckStore()

const searchValue = ref("")

const colorNames = ['green', 'yellow', 'orange', 'red', 'grey'];
const value = ref([3,6,1,12,9,3,6])

const cardValue = ref([3,6,1,12,9])
const labels = ["Easy", "Good", "Hard", "Again", "not learned"];

const allDecks = computed(() => DeckStore.getAllDecks())

const toDisplay = computed(() => {
  const items: Deck[] = [];
  for (let i = 0; i < allDecks.value.length; i++) {
    if (allDecks.value[i].title.includes(searchValue.value)) {
      items.push(allDecks.value[i])
    }
  }
  return items;
});

</script>
<template>
  <v-responsive>
    <Searchbar @change-value="searchValue=$event" />
    <v-expansion-panels class="pa-2 rounded-lg elevation-0">
      <v-expansion-panel
        expand-icon="mdi-chevron-down"
      >
        <v-expansion-panel-title>Deck Statistiken</v-expansion-panel-title>
        <v-expansion-panel-text>
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
                    style="max-width: 300px; min-height: 300px"
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
                        Lernfortschritt:
                        <div
                          class="progress_bar"
                          style="display:flex; max-width:250px; height:10px;  margin-left: -10px"
                        >
                          <!--                  geht durch liste der cards für den stapel : Aufbau [Anzahl grüne Karten,Anzahl gelbe Karten,Anzahl orange Karten,Anzahl rote Karten,Anzahl graue Karten]-->
                          <div
                            v-for="(count, color_index) in cardValue"
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
                    class="d-flex flex-column"
                    variant="elevated"
                    style="max-width: 300px; min-height: 300px"
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
                        Lernfortschritt:
                        <div
                          class="progress_bar"
                          style="display:flex; max-width:250px; height:10px;  margin-left: -10px"
                        >
                          <!--geht durch liste der cards für den stapel : Aufbau [Anzahl grüne Karten,Anzahl gelbe Karten,Anzahl orange Karten,Anzahl rote Karten,Anzahl graue Karten]-->
                          <div
                            v-for="(count, color_index) in cardValue"
                            :key="color_index"
                            :style="{
                              backgroundColor: colorNames[color_index],
                              flexGrow: count,
                              height: '100%'
                            }"
                          />
                        </div>
                      </v-col>
                      <v-expansion-panels elevation="0">
                        <v-expansion-panel>
                          <v-expansion-panel-title expand-icon="mdi-chevron-down">
                            Karteninformationen
                          </v-expansion-panel-title>
                          <v-expansion-panel-text>
                            <v-sheet>
                              <ColoredSparkline
                                :title="Definitionen"
                                :values="cardValue"
                                :labels="labels"
                                :bar-colors="colorNames"
                              />
                            </v-sheet>
                          </v-expansion-panel-text>
                        </v-expansion-panel>
                      </v-expansion-panels>
                    </v-card-text>
                  </v-card>
                </v-sheet>
              </v-col>
            </v-row>
          </v-container>
        </v-expansion-panel-text>
      </v-expansion-panel>

      <v-expansion-panel
        v-if="UserStore.authenticated"
        expand-icon="mdi-chevron-down"
      >
        <v-expansion-panel-title>Nutzer statistiken</v-expansion-panel-title>
        <v-expansion-panel-text>
          <v-card
            class="mx-auto text-center"
            color="primary"
            dark
            style="width: 100%; max-width: 600px"
          >
            <v-card-text>
              <v-sheet>
                <v-sparkline
                  :fill="false"
                  :gradient="['green', 'yellow', 'red']"
                  :line-width="2"
                  :model-value="value"
                  :padding="8"
                  :smooth="true"
                  auto-draw
                  show-labels
                >
                  {{ value }}
                </v-sparkline>
              </v-sheet>
            </v-card-text>
            <v-card-text>
              <div class="text-h4 font-weight-thin">
                Karten gelernt diese Woche
              </div>
            </v-card-text>
          </v-card>
        </v-expansion-panel-text>
      </v-expansion-panel>
    </v-expansion-panels>
  </v-responsive>
</template>

<style scoped lang="sass">
.learn-btn
  position: fixed
  bottom: 20px
  right: 20px
  z-index: 100

.no-word-break
  word-break: keep-all
  overflow-wrap: normal

</style>
