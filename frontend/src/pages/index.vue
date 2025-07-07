<script setup lang="ts">
import { useUserStore } from '@/stores/users'
import { onMounted } from 'vue'

const userStore = useUserStore()

onMounted(async () => {
  if (userStore.authenticated) {
    await userStore.loadProfile()
  }
})

</script>

<template>

  <Decks />
</template>
