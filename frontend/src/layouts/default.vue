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
              max-width="180"
            >
              <v-img
                max-height="55"
                color="white"
                src="@/assets/lexMeaLogo.png"
              />
            </v-card>
          </div>
        </v-toolbar-title>


        <div v-if="$vuetify.display.mdAndUp && userStore.authenticated">
          Hallo,  {{ userStore.username }}!
        </div>


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
            <div class="hide">
              Dashboard
            </div>
          </v-tab>
          <v-tab class="font-weight-light text-none font-weight-bold">
            <v-icon icon="mdi-plus-box-multiple" />
            <div class="hide">
              Karten hinzufügen
            </div>
          </v-tab>
          <v-tab class="font-weight-light text-none font-weight-bold">
            <v-icon
              icon="mdi-chart-bar"
            />
            <div
              id="statsHide"
              class="hide"
            >
              Statistiken
            </div>
          </v-tab>
          <v-tab class="font-weight-light text-none font-weight-bold">
            <v-icon
              icon="mdi-message-text"
            />
            <div
              class="hide"
            >
              Nachrichten
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

<style>


.hide { display:none; }

/* Desktop*/
@media screen and (min-width: 768px) {
  .hide  { display: block; }
}

</style>
