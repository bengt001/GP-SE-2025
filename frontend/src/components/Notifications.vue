<script setup lang="ts">
import {ref} from 'vue'
import {useUserStore} from "@/stores/users";

const UserStore = useUserStore()
const exampleNotes = ref([
  {
    rechtsgebiet: ['Strafrecht', 'Strafrecht AT'],
    cardsDue: 7,
    read: false,

  },
  {
    rechtsgebiet: ['Strafrecht', 'Strafrecht BT', "Nichtvermögensdelikte"],
    cardsDue: 10,
    read: true,
  }
])
const deleteNote = (index: number) => {
  exampleNotes.value.splice(index, 1)
}

function markAsRead(index: number) {
  if (!exampleNotes.value[index].read) {
    exampleNotes.value[index].read = true
  }
}

</script>

<template>
  <!--  Benachrichtigugen sind nur für angemeldete Nutzer sichtbar!-->
  <v-row>
    <v-col
      v-if="UserStore.authenticated"
    >
      <v-card
        v-for="(note, index) in exampleNotes"
        :key="index"
        class="mx-auto mb-4"
        @click="markAsRead(index)"
      >
        <v-card-title :class="{ 'read-text': note.read }">
          {{ note.cardsDue }} Karten sind heute fällig!
        </v-card-title>
        <v-btn
          icon
          size="small"
          color="grey"
          class="delete-btn position-absolute"
          style="top: 50%; right: 20px; transform: translateY(-50%)"
          @click="deleteNote(index)"
        >
          <v-icon>mdi-delete</v-icon>
        </v-btn>
        <v-card-text :class="{ 'read-text': note.read }">
          Der Stapel {{ note.rechtsgebiet[note.rechtsgebiet.length - 1] }} hat {{ note.cardsDue }} fällig Karten. Klicke
          hier,
          um sofot mit dem Lernen zu starten.
        </v-card-text>
      </v-card>
    </v-col>
    <v-col v-else>
      Melde dich an, um deine Benachrichtigungen zu sehen!
    </v-col>
  </v-row>
</template>

<style scoped lang="sass">
.delete-btn:hover
  background-color: #616161 !important
  color: white

.read-text
  color: rgba(0, 0, 0, 0.5)
</style>
