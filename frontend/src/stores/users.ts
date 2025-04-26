import {defineStore} from 'pinia'
import type Credentials from "@/types/Credentials";
import api from "@/api";

export const useUserStore = defineStore('users', {
    state: () => ({
        authenticated: Boolean(false),
    }),

    actions: {
        authenticate(token: string|null) : void { //<1>
            if (token !== null) {
                this.authenticated = true;
                localStorage.setItem('token', token);
                console.log("authenticated", token);
            } else {
                this.authenticated = false;
            }
        },

        requestToken(credentials: Credentials) : Promise<void> {
            return new Promise<void>((resolve, reject) => {
                api.authorization.login(credentials.email, credentials.password).then((res) => {
                  console.log('login', res);
                    this.authenticate(res.headers.authorization);
                    resolve();
                }).catch((error) => {
                  console.log("Fehler",error)
                    this.authenticate(null);
                    reject();
                })
            })
        }
    }
})
