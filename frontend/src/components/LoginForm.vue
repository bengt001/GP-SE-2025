<script setup lang="ts">
import {useUserStore} from "@/stores/users"
import {useDeckStore} from "@/stores/deck";

const deckStore = useDeckStore()
const userStore = useUserStore()
const email = ref('')
const username = ref('')
const password = ref('')
const login_snack = ref(false)
const tab = ref('login')
const passwordRepeat = ref('')
const registerRepeat_snack = ref(false)
const registerEmail_snack = ref(false)
const registerDone_snack = ref(false)
const validEmail_snack = ref(false)
const validPassword_snack = ref(false)
const loginSuccess_snack = ref(false)
const regexEmail = new RegExp("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")
const regexPassword = new RegExp("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}")


const router = useRouter()


function login() {
  let j;
  for (j = 0; j < email.value.length; j++) {
    if (email.value[j] === '@') {
      username.value = email.value.slice(0, j);
      break;
    }
  }
  userStore.requestToken({email: email.value, password: password.value, username: username.value}).then(() => {
    login_snack.value = false
    loginSuccess_snack.value = true
    userStore.setNameEmail(username.value, email.value);
    userStore.loadProfile();
    deckStore.abortDeckLoading()
    deckStore.loadMyDecks()
    setTimeout(() => {
      router.push('/')
    }, 1000)
  }).catch(() => {
    username.value = ''
    login_snack.value = true
  })
}

function register() {
  if (!regexEmail.test(email.value)) {
    validEmail_snack.value = true;
    return;
  }

  userStore.emailTaken(email.value).then((taken) => {
    if (taken) {
      registerEmail_snack.value = true;
    } else if (password.value !== passwordRepeat.value) {
      registerRepeat_snack.value = true;
    } else if (!regexPassword.test(password.value)) {
      validPassword_snack.value = true;
    } else {
      let j;
      for (j = 0; j < email.value.length; j++) {
        if (email.value[j] === '@') {
          username.value = email.value.slice(0, j);
          break;
        }
      }
      userStore.saveUsr({email: email.value, password: password.value, username: username.value})
        .then(() => {
          console.log('Saved')

          userStore.requestToken({email: email.value, password: password.value, username: username.value}).then(() => {
            console.log('Token requestet')
            registerDone_snack.value = true
            userStore.setNameEmail(username.value, email.value);
            userStore.loadProfile();
            deckStore.clear_decks();
            setTimeout(() => {
              router.push('/')
            }, 1000)
          }).catch(() => {
            console.log('Error')
            username.value = ''
          })
        });
    }
  }).catch(() => {
    registerEmail_snack.value = true;
  });
}


</script>

<template>
  <v-responsive>
    <v-card
      class="mx-auto my-10"
      elevation="15"
      color="white"
      max-width="344"
    >
      <v-tabs
        v-model="tab"
        color="primary"
      >
        <v-tab value="login">
          Login
        </v-tab>
        <v-tab value="register">
          Registrieren
        </v-tab>
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
                <v-btn
                  color="primary"
                  @click="login"
                >
                  Anmelden
                </v-btn>
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
                <v-btn
                  color="primary"
                  @click="register"
                >
                  Registrieren
                </v-btn>
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
      color="error"
    >
      Falscher Nutzername oder Passwort
    </v-snackbar>

    <v-snackbar
      v-model="loginSuccess_snack"
      :timeout="1000"
      class="elevation-24"
      color="success"
    >
      Login erfolgreich
    </v-snackbar>

    <v-snackbar
      v-model="registerRepeat_snack"
      :timeout="2000"
      class="elevation-24"
      color="error"
    >
      Passwörter stimmen nicht überein
    </v-snackbar>

    <v-snackbar
      v-model="registerEmail_snack"
      :timeout="2000"
      class="elevation-24"
      color="error"
    >
      Email existiert bereits
    </v-snackbar>

    <v-snackbar
      v-model="registerDone_snack"
      :timeout="2000"
      class="elevation-24"
      color="success"
    >
      Registrierung erfolgreich
    </v-snackbar>

    <v-snackbar
      v-model="validEmail_snack"
      :timeout="2000"
      class="elevation-24"
      color="error"
    >
      Kein gültiges Email format
    </v-snackbar>

    <v-snackbar
      v-model="validPassword_snack"
      :timeout="2000"
      class="elevation-24"
      color="error"
    >
      Das Passwort muss mindestens 8 Zeichen lang sein und mindestens eine Zahl und ein Buchstabe enthalten.
    </v-snackbar>
  </v-responsive>
</template>



