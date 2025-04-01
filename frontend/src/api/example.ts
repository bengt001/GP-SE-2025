import axios from './config'


export default {
  foo() {
    return axios.get("/api/foo")
  },
}
