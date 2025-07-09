<template>
  <v-responsive>
    <v-app>
      <v-app-bar color="primary">
        <v-app-bar-nav-icon
          variant="text"
          @click.stop="drawer = !drawer"
        />

        <v-toolbar-title>
          <div>
            <v-card
              max-width="150"
              class="rounded-0"
            >
              <v-img
                max-height="50"
                src="@/assets/lexMeaWebsiteLogo.png"
                alt="LexMea logo"
              />
            </v-card>
          </div>
        </v-toolbar-title>


        <div v-if="$vuetify.display.mdAndUp && userStore.authenticated">
          Hallo, {{ userStore.username }}!
        </div>

        <div>
          <router-link
            v-if="!userStore.authenticated"
            to="/login"
          >
            <v-btn
              icon
              color="white"
            >
              <v-avatar size="32">
                <img
                  src="/defaultIcon.svg"
                  alt="Profilbild"
                >
              </v-avatar>
            </v-btn>
          </router-link>

          <router-link
            v-else
            to="/profile"
          >
            <v-btn
              icon
              color="white"
            >
              <v-avatar size="32">
                <img
                  :src="(userStore.profilePicture ? userStore.profilePicture + '?t=' + profilePictureTimestamp : '/defaultIcon.svg')"
                  alt="Profilbild"
                  style="object-fit: cover; width: 100%; height: 100%;"
                  @error="e => (e.target as HTMLImageElement).src = '/defaultIcon.svg'"
                >
              </v-avatar>
            </v-btn>
          </router-link>
        </div>
      </v-app-bar>

      <v-navigation-drawer
        v-model="drawer"
        :location="$vuetify.display.mobile ? 'bottom' : undefined"
        temporary
        color="primary"
      >
        <v-list :items="items" />
      </v-navigation-drawer>

      <v-main>
        <!--        Subheader mit Home-Button und Buttons für Benachrichtigungen, Statistiken und Karteikartenhinzufügen -->
        <v-tabs
          v-if="!route.fullPath.includes('cards')"
          v-model="subheader"
          align-tabs="center"
          bg-color="lexmea_blue_300"
          stacked
        >
          <v-tab
            v-for="tab in subheader_tabs"
            :key="tab.title"
            class="font-weight-light text-none font-weight-bold"
            :to="tab.to"
          >
            <div style="position: relative; display: inline-block;">
              <v-icon>{{ tab.icon }}</v-icon>

              <!-- Roten Punkt nur bei Benachrichtigungen anzeigen, wenn User angemeldet und ungelesene Nachrichten vorhanden -->
              <span
                v-if="tab.title === 'Benachrichtigungen' && notificationStore.hasUnread && userStore.authenticated"
                class="red-dot"
                aria-label="unread notifications"
              />
            </div>
            <div v-if="$vuetify.display.mdAndUp">
              {{ tab.title }}
            </div>
          </v-tab>
        </v-tabs>
        <v-container>
          <router-view />
        </v-container>
      </v-main>
    </v-app>
  </v-responsive>
</template>

<script lang="ts" setup>
import {useUserStore} from "@/stores/users";
import {useNotificationStore} from "@/stores/notifications";
import {ref, watch} from 'vue'
import {profilePictureTimestamp} from '@/stores/profilePictureStore'


const userStore = useUserStore();
const notificationStore = useNotificationStore();


// Liste der Subheader-Tabs für for-Loop
const subheader = ref(' ')
const subheader_tabs = ref([
  {
    title: 'Dashboard',
    icon: 'mdi-home-circle',
    to: '/',
  },
  {
    title: 'Karten hinzufügen',
    icon: 'mdi-plus-box-multiple',
    to: '/adderpage',
  },
  {
    title: 'Statistiken',
    icon: 'mdi-chart-bar',
    to: '/stats',
  },
  {
    title: 'Benachrichtigungen',
    icon: 'mdi-message-text',
    to: '/notification',
  },
  {
    title: 'Freunde',
    icon: 'mdi-account-multiple',
    to: '/addfriend',
  }
])

const route = useRoute()

const drawer = ref(false)
const group = ref()
const items = ref([
  {
    title: 'Bibliothek',
    children: [
      {
        title: 'Gesetze',
      },
      {
        title: 'Inhalte',
      },
      {
        title: 'Chronik',
      }

    ]
  },
  {
    title: 'Arbeitsbereich'
  },
  {
    title: 'Lernbereich'
  },
])

watch(group, () => {
  drawer.value = false
})
</script>

<style scoped lang="sass">
.red-dot
  position: absolute
  top: 0
  right: 0
  width: 8px
  height: 8px
  background-color: red
  border-radius: 50%
</style>
