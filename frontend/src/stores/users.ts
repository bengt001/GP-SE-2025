import {defineStore} from 'pinia'
import type Credentials from "@/types/Credentials";
import api from "@/api";
import axios, {type AxiosResponse} from "axios";

export const useUserStore = defineStore('users', {
    state: () => ({
        authenticated: Boolean(false),
        username: String(''),
        email: String(''),
        id: String(''),
        totalXp: 0,
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

      async saveUsr(user: Credentials): Promise<AxiosResponse<Credentials>> {
        const usrCmd = {
          username: user.username,
          email: user.email,
          password: user.password
        };
        return await axios.post('/api/register', usrCmd);
      },
      logout() {
        this.authenticated = false;
        this.email = '';
        this.username = '';
        localStorage.removeItem('token')
        localStorage.removeItem('decks');
      },

      emailTaken(email: string): Promise<boolean> {
        return axios.get(`/api/exists`,{params: {email}})
          .then(res => res.data)
      },

      async earnXp(cardType: string, itemCount: number, rating: number): Promise<number> {
        const token = localStorage.getItem('token');

        const response = await axios.post('/api/xp/earn',
          {
            cardType,
            uncoveredItems: itemCount,
            rating
          },
          { headers: { Authorization: token } }
        );
        return response.data.gainedXp; //vorher .xp
      },

    }
})
