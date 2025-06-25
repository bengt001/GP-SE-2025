import {defineStore} from 'pinia'
import type Credentials from "@/types/Credentials";
import api from "@/api";
import axios, {type AxiosResponse} from "axios";

export const useUserStore = defineStore('users', {
    state: () => ({
        authenticated: Boolean(false),
        username: String(''),
        email: String(''),
    }),

    actions: {
      authenticate(token: string | null): void { //<1>
        if (token !== null) {
          this.authenticated = true;
          localStorage.setItem('token', token);
        } else {
          this.authenticated = false;
        }
      },

      requestToken(credentials: Credentials): Promise<void> {
        return new Promise<void>((resolve, reject) => {
          api.authorization.login(credentials.email, credentials.password).then((res) => {
            this.authenticate(res.headers.authorization);
            resolve();
          }).catch(() => {
            this.authenticate(null);
            reject();
          })
        })
      },

      setNameEmail(username: string, email: string): void {
        this.username = username;
        this.email = email;
      },

      saveUsr(user: Credentials): Promise<AxiosResponse<Credentials>> {
        const usrCmd = {
          username: user.username,
          email: user.email,
          password: user.password
        };
        return axios.post('/api/register', usrCmd);
      },
      logout() {
        this.authenticated = false;
        this.email = '';
        this.username = '';
        localStorage.removeItem('token')
      },

      emailTaken(email: string): Promise<boolean> {
        return axios.get(`/api/exists`,{params: {email}})
          .then(res => res.data)
      },

      async earnXp(cardType: string, itemCount: number, rating: number): Promise<number> { // <NEU>
        const token = localStorage.getItem('token');                                     // <NEU>
        const response = await axios.post('/api/xp/earn',                                // <NEU>
          { cardType, itemCount, rating },                                               // <NEU>
          { headers: { Authorization: token } }                                          // <NEU>
        );                                                                               // <NEU>
        return response.data.xp;                                                         // <NEU>
      }

    }
})
