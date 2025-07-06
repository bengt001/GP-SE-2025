import {defineStore} from 'pinia'
import type Notification from "@/types/Notification";
import axios from "axios";

export const useNotificationStore = defineStore('notification', {
  state: () => ({
    notes: [] as Notification[]
  }),
  actions: {
    async getNotifications(): Promise<void> {
      const token = localStorage.getItem('token')

      if (!token) {
        console.warn('No token found - user not authenticated')
        return
      }

      const response = await axios.get('/api/notification', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })

      console.log(response)

      this.notes = response.data
    },
    async markAsRead(index: number) {
      const note = this.notes[index]
      const token = localStorage.getItem('token')

      if (!token) {
        console.warn('No token found - user not authenticated')
        return
      }
      if (note && !note.read) {
        note.read = true

        try {
          await axios.put(`/api/notification/${note.id}/read`, null, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          })
          await this.getNotifications()
        } catch (error) {
          console.error('Failed to mark as read: ', error)
          note.read = false
        }
      }
    },

    async deleteNote(index: number) {
      const note = this.notes[index]
      const token = localStorage.getItem('token')

      if (!token) {
        console.warn('No token found - user not authenticated')
        return
      }

      try {
        await axios.delete(`/api/notification/${note.id}/delete`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        })
        await this.getNotifications()
      } catch (error) {
        console.error('Failed to delete notification: ', error);
      }
    },
    getMessages(index: number) {
      return this.notes[index].messages
    }
  },
  getters: {
    hasUnread(state) {
      return state.notes.some(note => !note.read)
    }
  }
})
