<template>
  <v-responsive>
    <v-app>
      <v-app-bar color="primary">
        <v-app-bar-nav-icon
          variant="text"
          @click.stop="drawer = !drawer"
        />

        <v-toolbar-title
          style="font-size: 20px;"
        >
          Willkommen {{ userStore.username }}
        </v-toolbar-title>


        <template v-if="$vuetify.display.mdAndUp">
          <v-btn
            icon="mdi-magnify"
            variant="text"
          />
        </template>

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
      </v-app-bar>

      <v-navigation-drawer
        v-model="drawer"
        :location="$vuetify.display.mobile ? 'bottom' : undefined"
        temporary
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
            to="index"
            class="font-weight-light text-none font-weight-bold"
          >
            <v-icon icon="mdi-home-circle" />
            Dashboard
          </v-tab>
          <v-tab class="font-weight-light text-none font-weight-bold">
            <v-icon icon="mdi-plus-box-multiple" />
            Karten hinzufügen
          </v-tab>
          <v-tab class="font-weight-light text-none font-weight-bold">
            <v-icon icon="mdi-chart-bar" />
            Statistiken
          </v-tab>
          <v-tab class="font-weight-light text-none font-weight-bold">
            <v-icon icon="mdi-message-text" />
            Nachrichten
          </v-tab>
        </v-tabs>
        <v-container>
          <router-view />
        </v-container>
      </v-main>
    </v-app>
  </v-responsive>
</template>


<script
  lang="ts"
  setup
>
import {useUserStore} from "@/stores/users";

const userStore = useUserStore();

const subheader = ref(' ')

const drawer = ref(false)
const group = ref()
const items = ref([
  {
    title: 'Foo',
    value: 'foo',
  },
  {
    title: 'Bar',
    value: 'bar',
  },
  {
    title: 'Fizz',
    value: 'fizz',
  },
  {
    title: 'Buzz',
    value: 'buzz',
  },
])

watch(group, () => {
  drawer.value = false
})
</script>
