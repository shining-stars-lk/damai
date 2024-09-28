import axios from 'axios';
import {sign} from '@/utils/request.js'
import { getToken } from '@/utils/auth'

axios.defaults.baseURL = import.meta.env.VITE_APP_URL

const service = axios.create({
  timeout: 40000,
  headers: {
    'X-Requested-With': 'XMLHttpRequest',
    'Content-Type': 'application/json; charset=UTF-8'
  },
})
service.interceptors.request.use(
  config => {
      //判断是否签名
      const signFlag = import.meta.env.VITE_SIGN_FLAG
      if (config.data != undefined && config.data != null && config.data != '' && signFlag == 1) {
          config.data = sign(config.data)
      }
      //向请求头放置token
      if (getToken) {
          let token = getToken();
          config.headers = Object.assign(config.headers,{token:token});
      }
    return config
  },
  error => {
    Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data;
    return res
  },
  error => {
  }
)
export default service
