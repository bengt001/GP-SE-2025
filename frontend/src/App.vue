<template>
  <router-view />
</template>

<script setup lang="ts">
import {onMounted} from 'vue';
import {useUserStore} from "@/stores/users"
import {useDeckStore} from "@/stores/deck";

const deckStore = useDeckStore()
const userStore = useUserStore()

onMounted(() => {
  if(!userStore.authenticated){
    userStore.logout()
    deckStore.reset_decks()
  }
})
</script>
