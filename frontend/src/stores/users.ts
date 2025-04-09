import {defineStore} from 'pinia'

export const useUserStore = defineStore('users', {
  state: () => ({
    authenticated: Boolean(false),
    registeredUsers: [
      {email: 'admin@test.com', password: 'password'},
      {email: 'test@test.com', password: '123456'},
    ]
  }),

  actions: {
    authenticate(email: string, password: string) {
      let i;
      for (i = 0; i < this.registeredUsers.length; i++) {
        if (this.registeredUsers[i].email === email && this.registeredUsers[i].password === password) {
          this.authenticated = true;
        }
      }
    },
    emailTaken(email: string): boolean {
      let i;
      for (i = 0; i < this.registeredUsers.length; i++) {
        if (this.registeredUsers[i].email === email) {
          return true;
        }
      }
      return false;
    }
  }
}
)
