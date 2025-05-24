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
            >
              <v-img
                max-height="55"
                color="white"
                src="@/assets/lexMeaWebsiteLogo.png"
              />
            </v-card>
          </div>
        </v-toolbar-title>


        <div v-if="$vuetify.display.mdAndUp && userStore.authenticated">
          Hallo,  {{ userStore.username }}!
        </div>


        <div v-if="$vuetify.display.mdAndUp">
          <v-btn
            icon="mdi-magnify"
            variant="text"
          />
        </div>

        <div>
          <router-link
            v-if="!userStore.authenticated"
            to="/login"
          >
            <v-btn
              icon="mdi-account"
              color="white"
            />
          </router-link>

          <router-link
            v-else
            to="/profile"
          >
            <v-btn
              icon="mdi-account"
              color="white"
            />
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
            <v-icon>
              {{ tab.icon }}
            </v-icon>
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

const userStore = useUserStore();

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
    to: ' ',
  },
  {
    title: 'Statistiken',
    icon: 'mdi-chart-bar',
    to: ' ',
  },
  {
    title: 'Benachrichtigungen',
    icon: 'mdi-message-text',
    to: ' ',
  }
])

const drawer = ref(false)
const group = ref()
const items = ref([
  {
    title: 'Bibliothek',
    value: 'foo',
    children: [
      {
        title: 'Gesetze',
      },
      {
        title:'Inhalte',
      },
      {
        title: 'Chronik',
      }

    ]
  },
  {
    title: 'Arbeitsbereich',
    value: 'fizz',
  },
  {
    title: 'Lernbereich',
    value: 'buzz',
  },
])

watch(group, () => {
  drawer.value = false
})
</script>
