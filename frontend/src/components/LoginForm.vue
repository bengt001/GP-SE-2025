<script setup lang="ts">
import {useUserStore} from "@/stores/users"

const userStore = useUserStore()
const email = ref('')
const password = ref('')
const login_snack = ref(false)
const tab = ref('login')
const passwordRepeat = ref('')
const registerRepeat_snack = ref(false)
const registerEmail_snack = ref(false)
const registerDone_snack = ref(false)
const validEmail_snack = ref(false)
const validPassword_snack = ref(false)
const loginSucess_snack = ref(false)
const regexEmail = new RegExp("^[^\s@]+@[^\s@]+\.[^\s@]+")
const regexPassword = new RegExp("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}")

const router = useRouter()

function login() {
  if (!regexEmail.test(email.value)) {
    validEmail_snack.value = true
  } else {
    userStore.authenticate(email.value, password.value)
    if (userStore.authenticated) {
      login_snack.value = false
      loginSucess_snack.value = true
      setTimeout(() => {
        router.push('/')
      },1000)
    } else {
      login_snack.value = true
    }
  }
}

function register() {
  if (!regexEmail.test(email.value)) {
    validEmail_snack.value = true
  } else {
    if (userStore.emailTaken(email.value)) {
      registerEmail_snack.value = true
    } else if (password.value !== passwordRepeat.value) {
      registerRepeat_snack.value = true
    } else if (!regexPassword.test(password.value)) {
      validPassword_snack.value = true
    } else {
      userStore.addUser(email.value, password.value)
      userStore.authenticate(email.value, password.value)
      registerDone_snack.value = true
      setTimeout(() => {
        router.push('/')
      }, 2000)
    }
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
            <v-text-field
              v-model="passwordRepeat"
              placeholder="Password wiederholen"
              type="password"
            />
            <v-col>
              <v-row justify="space-between">
                <v-btn class="blueButton" @click="register">Regestrieren</v-btn>
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
      Falscher Nutzername oder Passwort
    </v-snackbar>

    <v-snackbar
      v-model="loginSucess_snack"
      :timeout="1000"
      class="elevation-24"
      color="green-accent-4">
      Login erfolgreich
    </v-snackbar>

    <v-snackbar
      v-model="registerRepeat_snack"
      :timeout="2000"
      class="elevation-24"
      color="red-accent-4">
      Passwörter stimmen nicht überein
    </v-snackbar>

    <v-snackbar
      v-model="registerEmail_snack"
      :timeout="2000"
      class="elevation-24"
      color="red-accent-4">
      Email existiert bereits
    </v-snackbar>

    <v-snackbar
      v-model="registerDone_snack"
      :timeout="2000"
      class="elevation-24"
      color="green-accent-4">
      Registrierung erfolgreich
    </v-snackbar>

    <v-snackbar
      v-model="validEmail_snack"
      :timeout="2000"
      class="elevation-24"
      color="red-accent-4">
      Kein gültiges Email format
    </v-snackbar>

    <v-snackbar
      v-model="validPassword_snack"
      :timeout="2000"
      class="elevation-24"
      color="red-accent-4">
      Das Passwort muss mindestens 8 Zeichen lang sein und mindestens eine Zahl und ein Buchstabe enthalten.
    </v-snackbar>
  </v-responsive>
</template>

<style>
.blueButton {
  background-color: #054D63;
  color: white;
}
</style>

