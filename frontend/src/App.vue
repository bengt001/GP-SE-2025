<template>
  <router-view />
</template>

<script setup lang="ts">
import {onMounted} from 'vue';
import {useUserStore} from "@/stores/users"
import {useDeckStore} from "@/stores/deck";

onMounted(() => {
  const userStore = useUserStore()
  const deckStore = useDeckStore()

  const token = localStorage.getItem('token')
  userStore.authenticate(token) // this sets authenticated = true if token exists

  if (userStore.authenticated) {
    try {
      deckStore.loadMyDecks()
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
    } catch (error) {
      userStore.logout()
      deckStore.reset_decks()
    }
  } else {
    userStore.logout()
    deckStore.reset_decks()
  }
})
</script>
