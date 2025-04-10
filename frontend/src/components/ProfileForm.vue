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
  <v-container class="fill-height">
    <v-responsive class="align-centerfill-height mx-auto" max-width="900">
      <div class="text-center">
        <div>Profile</div>
        <div>Email: {{ userStore.email }}</div>
        <div>Username: {{ userStore.username }}</div>
        <v-btn
          class="blueButton"
          @click="logOut"
        >
          Log Out
        </v-btn>
      </div>

      <v-snackbar
        v-model="LogOutSuccess_snack"
        :timeout="1000"
        class="elevation-24"
        color="green-accent-4"
      >
        Log Out erfolgreich
      </v-snackbar>
    </v-responsive>
  </v-container>
</template>

<style>
.blueButton {
  background-color: #054D63;
  color: white;
}
</style>

