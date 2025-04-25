
import axios from './config'
import type {AxiosResponse} from "axios";

const authorization = {
  login(username: string, password: string) : Promise<AxiosResponse<any, any>> { //<1>
    const credentials : URLSearchParams = new URLSearchParams();
    credentials.append('username', username);
    credentials.append('password', password);
    return axios.post('/api/authenticate', credentials) //<2>
  }
}

export default authorization
