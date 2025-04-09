<script setup lang="ts">
import {useUserStore} from "@/stores/users"

const userStore = useUserStore()
const email = ref('')
const password = ref('')
const login_snack = ref(false)
const tab = ref('login')

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
      elevation="15"
      color="surface-variant"
      max-width="344"
    >
      <v-tabs v-model="tab" class="blueButton">
        <v-tab value="login">Login</v-tab>
        <v-tab value="register">Registrieren</v-tab>
      </v-tabs>
      <v-card-text>
        <v-tabs-window v-model="tab">
          <v-tabs-window-item value="login">
            <v-text-field
              v-model="email"
              placeholder="email"
            />
            <v-text-field
              v-model="password"
              placeholder="Password"
              type="password"
            />
            <v-col>
              <v-row justify="space-between">
                <v-btn class="blueButton" @click="login">Anmelden</v-btn>
              </v-row>
            </v-col>
          </v-tabs-window-item>

          <v-tabs-window-item value="register">
            <v-text-field
              v-model="email"
              placeholder="email"
            />
            <v-text-field
              v-model="password"
              placeholder="Password"
              type="password"
            />
            <v-col>
              <v-row justify="space-between">
                <v-btn class="blueButton" @click="login">Anmelden</v-btn>
              </v-row>
            </v-col>
          </v-tabs-window-item>
        </v-tabs-window>
      </v-card-text>
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
.blueButton {
  background-color: #054D63;
  color: white;
}
</style>

