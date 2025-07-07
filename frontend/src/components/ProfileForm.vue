<script setup lang="ts">
import {useUserStore} from "@/stores/users"
import {useDeckStore} from "@/stores/deck";

const deckStore = useDeckStore()
const userStore = useUserStore()
const router = useRouter()

const LogOutSuccess_snack = ref(false)

function logOut() {
  deckStore.abortDeckLoading()
  LogOutSuccess_snack.value = true
  setTimeout(() => {
    router.push('/')
    userStore.logout()
    deckStore.reset_decks()
  }, 1000)
}

onMounted(async () => {
  await userStore.loadProfile();
});
</script>

<template>
  <v-card
    class="mx-auto my-10 pa-6"
    elevation="15"
    color="white"
    max-width="344"
  >
    <v-responsive
      class="mx-auto"
      max-width="900"
    >
      <div class="text-center d-flex flex-column align-center">
        <!-- Streak in Chip-Form anzeigen (optimal für mobile ansicht) -->
        <v-chip
          color="orange lighten-3"
          text-color="black"
          v-if="userStore.streakCount > 0"
          class="mb-4"
        >
          <v-icon start>
            mdi-fire
          </v-icon>
          Streak: {{ userStore.streakCount }} {{ userStore.streakCount === 1 ? 'Tag' : 'Tage' }}
        </v-chip>
        <h3 class="mb-4">
          Profile
        </h3>

        <div class="mb-2">
          Email: {{ userStore.email }}
        </div>
        <div class="mb-4">
          Username: {{ userStore.username }}
        </div>
        <div class="mb-4">
          Gesammelte XP: {{ userStore.totalXp }}
        </div>

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

<style scoped>
.streak-badge {
  position: fixed;
  top: 16px;
  right: 16px;
  background: #fff3e0;
  color: #e65100;
  padding: 8px 12px;
  border-radius: 24px;
  display: flex;
  align-items: center;
  font-weight: bold;
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
  z-index: 9999;
}

.streak-text {
  font-size: 14px;
}
</style>
<style scoped>
.text-wrap {
  word-break: break-word;
  white-space: normal;
}
</style>


