<script setup lang="ts">
import {useDeckStore} from "@/stores/deck";
import {useUserStore} from "@/stores/users";
import Decks from "@/components/Decks.vue";

const UserStore = useUserStore()
const DeckStore = useDeckStore()
const DialogReset = ref(false)
const DialogDeactivate = ref(false)

const value = 50
const bufferValue = 70
const decks = ['Strafrecht AT', 'Strafrecht BT']
const faellig = ['Heute keine Karten fällig', '12 Karten fällig']

</script>

<template>
  <v-container>
    <v-row no-gutters>
      <!--      Dashboard for authenticated User-->
      <v-col v-if="UserStore.authenticated"
             v-for="n in decks.length"
             :key="n"
             cols="auto"
             sm="4">
        <v-sheet class="ma-2 pa-2"
                 color="background">
          <v-card
            min-width="300"
            max-width="300"
            height="400"
            class="d-flex flex-column"
            ref="card"
            variant="elevated"
          >
            <v-card-title class="text-h5">
              {{decks[n-1]}}
            </v-card-title>

            <v-card-text class="text-center">
              <v-col class="text-h6"
                     cols="auto">
              {{faellig[n-1]}}
              <v-progress-linear
                v-model="value"
                :buffer-value="bufferValue"
                color="success"
                buffer-color="red"
                buffer-opacity="100"
                bg-color="primary"
                :height="10"
              ></v-progress-linear>
              </v-col>
            </v-card-text>

            <v-card-actions class="mt-auto">
              <v-row justify="space-evenly">

                <v-btn class="align-content-center"
                       variant="flat"
                       color="lexmea_blue_200">
                  Lernen
                </v-btn>

                <v-btn class="align-content-center"
                       variant="flat"
                       color="red_darkest"
                       @click="DialogReset=true">
                  Reset
                </v-btn>

                <v-btn class="align-content-center"
                       variant="flat"
                       color="orange_darkest"
                       @click="DialogDeactivate=true">
                  Deaktivieren
                </v-btn>

              </v-row>
            </v-card-actions>
          </v-card>
        </v-sheet>
      </v-col>
      <!--      Dashboard for unauthenticated User-->
      <v-col
        v-if="!UserStore.authenticated"
        cols="auto"
        sm="4"
        v-for="n in 1"
        :key="n">
        <v-sheet class="ma-2 pa-2"
                 color="background">

          <v-card
            min-width="300"
            max-width="300"
            height="400"
            class="d-flex flex-column"
            ref="card"
            variant="elevated"
          >
            <v-card-title class="text-h5">
              {{decks[0]}}
            </v-card-title>

            <v-card-text class="text-center">
              <v-col class="text-h6"
                     cols="auto">
              8 fällige Karten
              <v-progress-linear
                v-model="value"
                :buffer-value="bufferValue"
                color="success"
                buffer-color="red"
                buffer-opacity="100"
                bg-color="primary"
                :height="10"></v-progress-linear>
              </v-col>
            </v-card-text>

            <v-card-actions class="mt-auto">
              <v-row justify="space-evenly">

                <v-btn class="align-content-center"
                variant="flat"
                color="lexmea_blue_200">
                  Lernen
                </v-btn>

                <v-btn class="align-content-center"
                       variant="flat"
                        color="red_darkest"
                @click="DialogReset=true">
                  Reset
                </v-btn>

                <v-btn class="align-content-center"
                       variant="flat"
                       color="orange_darkest"
                @click="DialogDeactivate=true">
                  Deaktivieren
                </v-btn>

              </v-row>
            </v-card-actions>
          </v-card>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>

  <v-dialog v-model="DialogDeactivate"
  max-width="344">
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

  <v-dialog v-model="DialogReset"
  max-width="344">
    <v-card>
      <v-card-title>
        Karten reseten
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
