<script setup lang="ts">
import {useDeckStore} from "@/stores/deck";
import {useUserStore} from "@/stores/users";
const UserStore = useUserStore()
const DeckStore = useDeckStore()
</script>

<template>
    <v-row dense>
      <v-col
        v-if="UserStore.authenticated"
        v-for="deck in DeckStore.decks" :key="deck.stapel_id"
        cols="12">
        <v-card color="#385F73">
          <v-card-title class="text-h5">
            Unlimited music now
          </v-card-title>

          <v-card-subtitle>
            Listen to your favorite artists and albums whenever and wherever, online and offline.
          </v-card-subtitle>

          <v-card-actions>
            <v-btn text="Listen Now" variant="text"></v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
      <v-col
      v-if="!UserStore.authenticated"
      cols="12">
        <v-card color="#385F73">
          <v-card-title class="text-h5">
            Strafrecht AT
          </v-card-title>

          <v-card-subtitle>
            Listen to your favorite artists and albums whenever and wherever, online and offline.
          </v-card-subtitle>

          <v-card-actions>
            <v-btn text="Lernen" variant="text"></v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
</template>

<style scoped lang="sass">

</style>
