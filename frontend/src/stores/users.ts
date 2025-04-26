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

      saveUsr(user: Credentials): Promise<AxiosResponse<Credentials, any>> {
        const usrCmd = {
          username: user.username,
          email: user.email,
          password: user.password
        };
        return axios.post('/api/users', usrCmd);
      },
      logout() {
        this.authenticated = false;
        this.email = '';
        this.username = '';
      },

      emailTaken(email: string): Promise<boolean> {
        return Promise.resolve(false);
      }

    }
})
