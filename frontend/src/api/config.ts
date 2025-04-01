import axios from 'axios';
import type {AxiosError} from 'axios';

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_ENDPOINT,
});
instance.interceptors.request.use((config) => {
  const token : string|null = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
instance.interceptors.response.use(
  (res) => res,
  (error: AxiosError) => {
    const { data, status} = error.response!;
    switch (status) {
      case 400:
        console.error(data);
        break;
      case 401:
        console.error('unauthorised');
        break;
      case 403:
        console.error('forbidden');
        break;
      case 404:
        console.error('/not-found');
        break;
      case 500:
        console.error('/server-error');
        break;
      default:
        console.error(`Error. Status: ${status}`)
    }
    return Promise.reject(error);
  }
);
export default instance
