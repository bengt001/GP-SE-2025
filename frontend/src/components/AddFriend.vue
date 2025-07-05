<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/users'
import axios from 'axios'

const userStore = useUserStore()
const email = ref('')
const successSnack = ref(false)
const errorSnack = ref(false)
const errorText = ref('')
const pendingRequests = ref<{ id: number, requester: { email: string } }[]>([])
const friends = ref<{ email: string }[]>([])

async function sendFriendRequest() {
  const token = localStorage.getItem('token')
  if (!token) {
    errorText.value = 'Sie sind nicht angemeldet'
    errorSnack.value = true
    return
  }

  try {
    await axios.post('/api/friends/send', {
      requester: userStore.email,
      recipient: email.value.trim()
    }, {
      headers: { Authorization: token }
    })
    successSnack.value = true
    email.value = ''
    await fetchPendingRequests()
    await fetchFriends()
  } catch (err: unknown) {
    if (axios.isAxiosError(err)) {
      errorText.value = err.response?.data || 'Fehler beim Senden der Anfrage'
    } else {
      errorText.value = 'Ein unbekannter Fehler ist aufgetreten'
    }
    errorSnack.value = true
  }
}

async function fetchPendingRequests() {
  try {
    const response = await axios.get('/api/pending', {
      params: { email: userStore.email }
    })
    pendingRequests.value = response.data
  } catch (err) {
    console.error('Fehler beim Abrufen der Anfragen:', err)
  }
}

async function fetchFriends() {
  try {
    const response = await axios.get('/api/list', {
      params: { email: userStore.email }
    })
    friends.value = response.data
  } catch (err) {
    console.error('Fehler beim Abrufen der Freunde:', err)
  }
}

async function acceptRequest(requestId: number) {
  try {
    await axios.post('/api/accept', { requestId })
    await fetchPendingRequests()
    await fetchFriends()
  } catch (err) {
    console.error('Fehler beim Annehmen:', err)
  }
}

async function declineRequest(requestId: number) {
  try {
    await axios.post('/api/decline', { requestId })
    await fetchPendingRequests()
  } catch (err) {
    console.error('Fehler beim Ablehnen:', err)
  }
}

onMounted(() => {
  fetchPendingRequests()
  fetchFriends()
})
</script>

<template>
  <div v-if="userStore.authenticated">
    <v-card
      class="mx-auto my-10"
      elevation="15"
      color="white"
      max-width="344"
    >
      <v-card-title>Freund hinzufügen</v-card-title>
      <v-card-text>
        <v-text-field
          v-model="email"
          label="Email"
          type="email"
          autocomplete="off"
        />
        <v-btn
          color="primary"
          block
          @click="sendFriendRequest"
        >
          Anfrage senden
        </v-btn>
      </v-card-text>
    </v-card>

    <v-snackbar
      v-model="successSnack"
      :timeout="2000"
      class="elevation-24"
      color="success"
    >
      Freundschaftsanfrage gesendet
    </v-snackbar>

    <v-snackbar
      v-model="errorSnack"
      :timeout="3000"
      class="elevation-24"
      color="error"
    >
      {{ errorText }}
    </v-snackbar>

    <v-row
      class="my-10"
      justify="center"
    >
      <v-col
        cols="12"
        md="6"
      >
        <v-card
          v-if="pendingRequests.length"
          elevation="10"
          color="white"
        >
          <v-card-title>Eingehende Anfragen</v-card-title>
          <v-card-text>
            <div
              v-for="request in pendingRequests"
              :key="request.id"
              class="friend-request-row"
            >
              <div class="user-info">
                {{ request.requester.email }}
              </div>
              <div class="accept-decline">
                <v-btn
                  small
                  color="#E0EEE6"
                  @click="acceptRequest(request.id)"
                >
                  Annehmen
                </v-btn>
                <v-btn
                  small
                  color="#FFDFD5"
                  @click="declineRequest(request.id)"
                >
                  Ablehnen
                </v-btn>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col
        cols="12"
        md="4"
      >
        <v-card
          v-if="friends.length"
          elevation="10"
          color="white"
        >
          <v-card-title>Deine Freunde</v-card-title>
          <v-card-text>
            <div
              v-for="friend in friends"
              :key="friend.email"
              class="user-info mb-2"
            >
              👤 {{ friend.email }}
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
  <div v-else>
    Melde dich an, um deine Freundeliste zu sehen!
  </div>
</template>

<style scoped>
.friend-request-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
}

.user-info {
  flex: 1 1 auto;
  min-width: 0;
  word-break: break-word;
}

.accept-decline {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 120px;
  flex-shrink: 0;
}
</style>
