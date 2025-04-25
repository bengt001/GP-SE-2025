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
            } else {
                this.authenticated = false;
            }
        },

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
})
