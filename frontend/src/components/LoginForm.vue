<script setup lang="ts">
import {useUserStore} from "@/stores/users"

const userStore = useUserStore()
const email = ref('')
const password = ref('')
const login_snack = ref(false)

const router = useRouter()

function login() {
  userStore.authenticate(email.value, password.value)
  if (userStore.authenticated) {
    login_snack.value = false
    router.push('/')
  } else {
    login_snack.value = true
  }
}
</script>

<template>
  <v-responsive>
    <v-card
      class="mx-auto my-10"
      elevation="12"
      color="surface-variant"
      max-width="344"
      title="Login"
    >
      <template #actions>
        <v-col>
          <v-text-field
            v-model="email"
            placeholder="email"
          />
          <v-text-field
            v-model="password"
            placeholder="Password"
            type="password"
          />
          <v-row justify="space-between">
            <v-btn class="loginButton" @click="login">Anmelden</v-btn>
          </v-row>
        </v-col>
      </template>
    </v-card>
    <v-snackbar
      v-model="login_snack"
      :timeout="2000"
      class="elevation-24"
      color="red-accent-4"
    >
      Wrong username or password
    </v-snackbar>
  </v-responsive>
</template>

<style>
.loginButton {
  background-color: #054D63;
  color: white;
}
</style>

