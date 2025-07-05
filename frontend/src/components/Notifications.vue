<script setup lang="ts">
import {useUserStore} from "@/stores/users";
import { useNotificationStore } from '@/stores/notifications'

const notificationStore = useNotificationStore()
const UserStore = useUserStore()

</script>

<template>
  <!--  Benachrichtigugen sind nur für angemeldete Nutzer sichtbar!-->
  <v-row>
    <v-col
      v-if="UserStore.authenticated"
    >
      <v-card
        v-for="(note, index) in notificationStore.notes"
        :key="index"
        class="mx-auto mb-4"
        @click="notificationStore.markAsRead(index)"
      >
        <v-card-title
          v-if="note.type === 'WELCOME'"
          :class="{ 'read-text': note.read }"
        >
          Herzlich Willkommen!
        </v-card-title>
        <v-card-title
          v-else-if="note.type === 'DUECARDS'"
          :class="{ 'read-text': note.read }"
        >
          {{ note.title }}
        </v-card-title>
        <v-card-title
          v-else
          :class="{ 'read-text': note.read }"
        >
          Heute sind Karten fällig!
        </v-card-title>
        <v-btn
          icon
          size="small"
          color="grey"
          class="delete-btn position-absolute"
          style="top: 50%; right: 20px; transform: translateY(-50%)"
          @click.stop="notificationStore.deleteNote(index)"
        >
          <v-icon>mdi-delete</v-icon>
        </v-btn>
        <v-card-text
          v-if="note.type === 'WELCOME'"
          :class="{ 'read-text': note.read }"
        >
          {{ note.messages?.[0] }}
        </v-card-text>
        <v-card-text
          v-else
          :class="{ 'read-text': note.read }"
        >
          Du hast Karten, die zum Lernen bereit sind.
          Klicke hier, um sofort mit dem Lernen zu starten.
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
