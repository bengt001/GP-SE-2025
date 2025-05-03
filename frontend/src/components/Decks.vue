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
const title = "Strafrecht AT"

function openResetDialog(title: string) {

}

</script>

<template>
  <v-container>
    <v-row no-gutters>
      <v-col v-if="UserStore.authenticated"
             v-for="deck in DeckStore.decks" :key="deck.stapel_id"
             cols="12"
             sm="4">
        <v-sheet class="ma-2 pa-2">
          <v-card color="#385F73">
            <v-card-title class="text-h5">
              Unlimited music now
            </v-card-title>

            <v-card-subtitle>
            </v-card-subtitle>

            <v-card-actions>
              <v-btn text="Listen Now" variant="text"></v-btn>
            </v-card-actions>
          </v-card>
        </v-sheet>
      </v-col>
      <v-col
        v-if="!UserStore.authenticated"
        cols="auto"
        sm="4"
        v-for="n in 5"
        :key="n">
        <v-sheet class="ma-2 pa-2"
                 color="background">
          <v-card
            max-width="344"
            height="400"
            class="d-flex flex-column"
            ref="card"
            variant="elevated"
          >
            <v-card-title class="text-h5">
              {{title}}
            </v-card-title>

            <v-card-text class="text-center">
              <v-progress-linear
                v-model="value"
                :buffer-value="bufferValue"
                color="success"
                buffer-color="red"
                bg-color="primary"
                :height="10"
              ></v-progress-linear>
            </v-card-text>

            <v-card-actions class="mt-auto">
              <v-row justify="space-around">

                <v-btn class="align-content-center"
                variant="flat"
                color="lexmea_blue_200">
                  Lernen
                </v-btn>

                <v-btn class="align-content-center"
                       variant="flat"
                        color="red_darkest"
                @click="openResetDialog(title)">
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
        Sind Sie sicher, dass Sie diesen Stapel deaktivieren möchten?
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
        Sind Sie sicher, dass Sie diesen Stapel reseten möchten?
      </v-card-text>
      <v-card-actions>
        <v-btn @click="resetCards()">
          Ja, reseten
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
