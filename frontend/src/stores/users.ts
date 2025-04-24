import {defineStore} from 'pinia'
import type Credentials from "@/types/Credentials";
import api from "@/api";

export const useUserStore = defineStore('users', {
  state: () => ({
    authenticated: Boolean(false),
    username: String(''),
    email: String(''),
    registeredUsers: [
      {email: 'admin@test.com', password: 'password'},
      {email: 'test@test.com', password: '123456'},
    ]
  }),
  /*
  actions: {
    authenticate(taken: string|null) : void {
      if (token != null) {
        this.authenticated = true;
        this.email = email;
        let j;
        for (j = 0; j < email.length; j++) {
            if (email[j] === '@') {
              this.username = email.slice(0, j);
              break;
            }
          }
        localStorage.setItem('token',token);
      } else {
        this.authenticated = false;
        }
      },

      logout() : void {
      this.authenticated = false;
      this.username = '';
      this.email = '';
      localStorage.removeItem('token');
      }

      // email taken , addUser müssen noch gemacht werden
      requestToken(credentials: Credentials) : Promise<void> {
         return new Promise<void>((resolve, reject) => {
          api.authorization.login(credentials.username, credentials.password).then((res) => {
            this.authenticate(res.headers.authorization);
            resolve();
          }).catch(() => {
            this.authenticate(null);
            reject();
           })
          })
         }
       }
   */
  actions: {
    authenticate(email: string, password: string) {
      let i;
      for (i = 0; i < this.registeredUsers.length; i++) {
        if (this.registeredUsers[i].email === email && this.registeredUsers[i].password === password) {
          this.authenticated = true;
          this.email = email;
          let j;
          for (j = 0; j < email.length; j++) {
            if (email[j] === '@') {
              this.username = email.slice(0, j);
              break;
            }
          }
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
    },
    addUser(email: string,password: string) {
      this.registeredUsers.push({email, password});
    },
    logout() {
      this.authenticated = false;
      this.username = '';
      this.email = '';
    },
  }
}
)
