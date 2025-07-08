<script setup lang="ts">
import {useUserStore} from "@/stores/users"
import {useDeckStore} from "@/stores/deck";
import {profilePictureTimestamp} from '@/stores/profilePictureStore'


const deckStore = useDeckStore()
const userStore = useUserStore()
const router = useRouter()

const LogOutSuccess_snack = ref(false)

//const profilePictureTimestamp = ref(Date.now())
const profilePictureFile = ref<File | null>(null)
const dialog = ref(false)
const uploadSnack = ref(false)
const uploadErrorSnack = ref(false)
const uploadErrorText = ref('')
const defaultAvatar = '/defaultIcon.svg'


function logOut() {
  deckStore.abortDeckLoading()
  LogOutSuccess_snack.value = true
  setTimeout(() => {
    router.push('/')
    userStore.logout()
    deckStore.reset_decks()
  }, 1000)
}

async function uploadProfilePicture() {
  if (!profilePictureFile.value) return

  try {
    await userStore.setProfilePicture(profilePictureFile.value)
    await userStore.loadProfile()
    console.log('New profilePicture URL:', userStore.profilePicture)
    profilePictureTimestamp.value = Date.now()
    uploadSnack.value = true
    profilePictureFile.value = null
    dialog.value = false // Close dialog on success
  } catch (err: unknown) {
    const message = (err as { response?: { data?: { message?: string } } })?.response?.data?.message;
    uploadErrorText.value = message || 'Fehler beim Hochladen';
    uploadErrorSnack.value = true;
  }
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
      <!-- Profile Picture -->
      <div class="d-flex justify-center">
        <v-avatar
          size="96"
          class="mx-auto mb-4"
          style="cursor: pointer;"
          @click="dialog = true"
        >
          <img
            :src="(userStore.profilePicture || defaultAvatar) + '?t=' + profilePictureTimestamp"
            alt="Profile picture"
            style="object-fit: cover; width: 100%; height: 100%;"
            @error="e => { if (e.target) (e.target as HTMLImageElement).src = defaultAvatar }"
          >
        </v-avatar>
      </div>
      <v-dialog
        v-model="dialog"
        max-width="400px"
      >
        <v-card>
          <v-card-title>Profilbild ändern</v-card-title>
          <v-card-text>
            <v-file-input
              v-model="profilePictureFile"
              label="Bild auswählen"
              accept="image/png, image/jpeg, image/gif"
              prepend-icon="mdi-camera"
              show-size
              dense
            />
          </v-card-text>
          <v-card-actions>
            <v-spacer />
            <v-btn
              variant="text"
              @click="dialog = false"
            >
              Abbrechen
            </v-btn>
            <v-btn
              color="primary"
              :disabled="!profilePictureFile"
              @click="uploadProfilePicture"
            >
              Hochladen
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <div class="text-center d-flex flex-column align-center">
        <!-- Streak in Chip-Form anzeigen (optimal für mobile ansicht) -->
        <v-chip
          v-if="userStore.streakCount > 0"
          color="orange lighten-3"
          text-color="black"
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
  <v-snackbar
    v-model="uploadSnack"
    :timeout="2000"
    color="success"
  >
    Profilbild erfolgreich hochgeladen!
  </v-snackbar>
  <v-snackbar
    v-model="uploadErrorSnack"
    :timeout="3000"
    color="error"
  >
    {{ uploadErrorText }}
  </v-snackbar>
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
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
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


