import {defineStore} from 'pinia'

export const useNotificationStore = defineStore('notification', {
    state: () => ({
        exampleNotes: [
            {
                rechtsgebiet: ['Strafrecht', 'Strafrecht AT'],
                cardsDue: 7,
                read: false
            },
            {
                rechtsgebiet: ['Strafrecht', 'Strafrecht BT', "Nichtvermögensdelikte"],
                cardsDue: 10,
                read: true,
            }
        ],
    }),
    actions: {
        markAsRead(id
                   :
                   number
        ) {
            const note = this.exampleNotes[id]
            if (note) note.read = true
        },

        deleteNote(index: number) {
            this.exampleNotes.splice(index, 1)
        },
    },
    getters: {
        hasUnread(state) {
            return state.exampleNotes.some(note => !note.read)
        }
    }
})
