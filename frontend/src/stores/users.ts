import { defineStore } from 'pinia'

export const useUserStore = defineStore('users', {
  state: () => ({
    authenticated: Boolean(false),
  }),

  actions: {
    authenticate(email: string, password: string) {
      this.authenticated = (email === 'admin@test.com' && password === 'password')
    }
  }
})
