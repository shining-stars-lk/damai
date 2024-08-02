import axios from 'axios'
import {jsrsasign,KJUR, hextob64} from "jsrsasign";
import { getToken } from '@/utils/auth'
import useUserStore from '@/store/modules/user'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 是否显示重新登录
export let isRelogin = { show: false };
// 创建一个 axios 实例
const request = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API, // 这里设置为代理的路径
    timeout: 10000, // 请求超时时间
    headers: import.meta.env.VITE_SIGN_FLAG == 1 ? {no_verify: false} : {no_verify: true}
});

// 请求拦截器
request.interceptors.request.use(
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
        return config;
    },
    error => {
        console.log(error); // 打印错误日志
        Promise.reject(error).then(r => {});
    }
);

// 响应拦截器
request.interceptors.response.use(
    response => {
        // 在这里可以做一些统一的响应处理逻辑
        let url = response.config.url;
        if ('/damai/user/user/logout' == url) {
            return response.data;
        }
        const code = response.data.code
        if(code == "10055"||code == "516"){
            if (!isRelogin.show) {
                isRelogin.show = true;
                ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', { confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning' })
                    .then(() => {
                    isRelogin.show = false;
                        console.log(useUserStore().logOut());
                        useUserStore().logOut().then(() => {
                          location.href='/login'
                    })
                }).catch(() => {
                    isRelogin.show = false;
                });
            }
            return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
        }else{
            return response.data;
        }

    },
    error => {
        console.log(error); // 打印错误日志
        return Promise.reject(error);
    }
);

export function sign(params){
    const code = import.meta.env.VITE_CODE
    const paramsStr = JSON.stringify(params)
    const signParam = {businessBody: paramsStr, code: code}

    const sig = new KJUR.crypto.Signature({alg: "SHA256withRSA"});
    sig.init("-----BEGIN PRIVATE KEY-----"+import.meta.env.VITE_SIGN_SECRET_KEY+"-----END PRIVATE KEY-----");
    sig.updateString(buildParam(signParam));
    let sign = hextob64(sig.sign());

    return {code: code, businessBody: paramsStr, sign: sign};
}

const buildKeyValue = (key, value, isEncode) => {
    let result = `${key}=`;
    if (isEncode) {
        try {
            result += encodeURIComponent(value);
        } catch (error) {
            result += value;
        }
    } else {
        result += value;
    }
    return result;
};

const buildParam = (params) => {
    const keys = Object.keys(params).sort();
    let queryString = '';

    keys.forEach((key, index) => {
        const value = params[key];
        queryString += buildKeyValue(key, value, false);
        if (index < keys.length - 1) {
            queryString += '&';
        }
    });

    return queryString;
};

export default request
