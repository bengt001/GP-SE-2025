<script setup lang="ts">
const tab = ref('adder')

import { ref } from 'vue'

const selected_creator = ref([])
const selected = shallowRef([])
const no_creator_snack = ref(false)
const router = useRouter()

const strafrecht = ref([
  {
    id: 1,
    title: 'Strafrecht AT',

  },
  {
    id: 2,
    title: 'Strafrecht BT',
    children: [
      { id: 3, title: 'Nichtvermögensdelikte'},
      { id: 4, title: 'Vermögensdelikte'},
        ],
  },
  {
        id: 5,
        title: 'Strafprozessrecht',

  },
])

const oeffirecht = ref([
  {
    id: 1,
    title: 'Verfassungsrecht',
    children: [
      { id: 2, title: 'Staatrecht I: Staatsorganisationsrecht'},
      { id: 3, title: 'Staatrecht II: Grundrechte'},
    ],
  },
  {
    id: 4,
    title: 'Verwaltungsrecht',
    children: [
      { id: 5, title: 'Allgemeines Verwaltungsrecht'},
      { id: 6,
        title: 'Besonderes Verwaltungsrecht',
      children: [
        { id: 7, title: 'Baurecht'}
      ]},
      { id: 8, title: 'Verwaltungsprozessrecht'},
    ],
  },
])

const zivilrecht = ref([
  {
    id: 1,
    title: 'Bürgerliches Recht',
    children: [
      {id: 2, title: 'BGB AT'},
      {
        id: 3,
        title: 'Schuldrecht',
        children: [
          {id: 4, title: 'Schuldrecht AT'},
          {
            id: 5,
            title: 'Schuldrecht BT',
            children: [
              {
                id: 6,
                title: 'Vertragliche Schuldverhältnisse',
                children: [
                  {id: 7, title: 'Kaufrecht u.Ä.'}
                ]
              }
            ]
          },
        ],
      },
      { id: 8, title: 'Sachenrecht'},
    ],
  },
  {
    id: 9,
    title: 'Zivilprozessrecht',
    children: [
      {id: 10, title: 'Nat. Zivilprozessrecht'}
    ]
  }
])

function addCards() {
    console.log(selected_creator)
    console.log(selected)

  if (selected_creator.value.length === 0) {
    no_creator_snack.value = true;
  } else {
    router.go(-1)
  }
}



</script>

<template>
  <v-responsive>
  <v-card>
   <v-tabs v-model="tab" bg-color="primary">
     <v-tab value="Strafrecht">
       Strafrecht
     </v-tab>
     <v-tab value="Öffentliches Recht">
       Öffentliches Recht
     </v-tab>
     <v-tab value="Zivilrecht">
       Zivilrecht
     </v-tab>
   </v-tabs>
    <v-card-text>
      <v-tabs-window v-model="tab">
        <v-tabs-window-item value="Strafrecht">
          <v-treeview
            :items="strafrecht"
            item-value="id"
            selectable
            select-strategy="classic"
            return-object
            v-model:selected="selected"
          ></v-treeview>
        </v-tabs-window-item>
        <v-tabs-window-item value="Öffentliches Recht">
          <v-treeview
            :items="oeffirecht"
            item-value="id"
            selectable
            select-strategy="classic"
            return-object
            v-model:selected="selected"
          ></v-treeview>
        </v-tabs-window-item>
        <v-tabs-window-item value="Zivilrecht">
          <v-treeview
            :items="zivilrecht"
            item-value="id"
            selectable
            select-strategy="classic"
            return-object
            v-model:selected="selected"
          ></v-treeview>
        </v-tabs-window-item>
      </v-tabs-window>
    </v-card-text>
    <v-card-actions>
      <v-row>
      <v-btn @click="addCards">
        Karteikarten aktivieren
      </v-btn>
        <v-select
          v-model="selected_creator"
          label="Ersteller"
          :items="['LexMea', 'Professor*in', 'Privat']"
          multiple></v-select>
      </v-row>
    </v-card-actions>
  </v-card>
  </v-responsive>

  <v-snackbar
    v-model="no_creator_snack"
    :timeout="2000"
    class="elevation-24"
    color="error"
  >
    Wähle einen Ersteller für die Karteikarten aus
  </v-snackbar>

</template>

<style scoped lang="sass">

</style>
