<template>
  <v-responsive class="border rounded">
    <v-app>
      <v-app-bar color="primary">
        <v-app-bar-nav-icon
          variant="text"
          @click.stop="drawer = !drawer"
        ></v-app-bar-nav-icon>

        <v-toolbar-title>Welcome Message Placeholder</v-toolbar-title>

        <v-spacer></v-spacer>

        <template v-if="$vuetify.display.mdAndUp">
          <v-btn icon="mdi-magnify" variant="text"></v-btn>
        </template>

        <router-link to="/login"><v-btn icon="mdi-account" variant="text"></v-btn></router-link>
      </v-app-bar>

      <v-navigation-drawer
        v-model="drawer"
        :location="$vuetify.display.mobile ? 'bottom' : undefined"
        temporary
      >
        <v-list :items="items"></v-list>
      </v-navigation-drawer>

      <v-main>
        <v-container>
          <router-view/>
        </v-container>
      </v-main>
    </v-app>
  </v-responsive>
</template>

<script lang="ts" setup>
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
