import {defineStore} from 'pinia'
import type Notification from "@/types/Notification";
import axios from "axios";

export const useNotificationStore = defineStore('notification', {
    state: () => ({
        notes: [] as Notification[]
    }),
    actions: {
      async getNotifications() :Promise<void> {
        const token = localStorage.getItem('token')

        if (!token) {
          console.warn('No token found — user not authenticated')
          return
        }

        const response = await axios.get('/api/notification', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        })

        this.notes = response.data
      },
        markAsRead(id: number) {
            const note = this.notes[id]
            if (note) note.read = true
        },

        deleteNote(index: number) {
            this.notes.splice(index, 1)
        },
    },
    getters: {
        hasUnread(state) {
            return state.notes.some(note => !note.read)
        }
    }
})
