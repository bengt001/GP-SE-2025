<script setup lang="ts">
import {useUserStore} from "@/stores/users"

const userStore = useUserStore()
const router = useRouter()

const LogOutSuccess_snack = ref(false)

function logOut() {
  LogOutSuccess_snack.value = true
  setTimeout(() => {
    router.push('/')
    userStore.logout()
  }, 1000)
}
</script>

<template>
  <v-card
    class="mx-auto my-10 pa-6"
    elevation="15"
    color="white"
    max-width="344"
  >
    <v-responsive class="mx-auto" max-width="900">
      <div class="text-center">
        <h3 class="mb-4">Profile</h3>

        <div class="mb-2">Email: {{ userStore.email }}</div>
        <div class="mb-4">Username: {{ userStore.username }}</div>

        <v-btn
          color="primary"
          @click="logOut"
        >
          Log Out
        </v-btn>
      </div>
    </v-responsive>

    <v-snackbar
      v-model="LogOutSuccess_snack"
      :timeout="1000"
      class="elevation-24"
      color="success"
    >
      Log Out erfolgreich
    </v-snackbar>
  </v-card>
</template>



