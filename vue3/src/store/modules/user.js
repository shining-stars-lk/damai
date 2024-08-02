import { login,logout} from '@/api/login'
import { getToken, setToken, removeToken,getName,setName,removeName,
    getUserIdKey,setUserIdKey,removeUserIdKey } from '@/utils/auth'
import { defineStore } from 'pinia'

const useUserStore = defineStore(
    'user',
    {
        state: () => ({
            token: getToken(),
            name: getName(),
            avatar: '',
            userId: getUserIdKey(),
            roles: [],
            permissions: []
        }),
        actions: {
            // 登录
            login(userInfo) {
                //email, mobile, password, code
                const email = userInfo.email
                const mobile = userInfo.mobile
                const password = userInfo.password
                const code = userInfo.code
                return new Promise((resolve, reject) => {
                    login(email,mobile, password, code).then(res => {
                        if(res.code == 0){
                            setToken(res.data.token)
                            userInfo.mobile? setName(userInfo.mobile):setName(userInfo.email)
                            setUserIdKey(res.data.userId)
                            this.token = res.data.token
                            this.userId = res.data.userId
                            resolve()
                        }else{
                            ElMessage.error(res.message)
                        }

                    }).catch(error => {
                        reject(error)
                    })
                })
            },
            // // 获取用户信息
            // getInfo() {
            //     return new Promise((resolve, reject) => {
            //         getInfo().then(res => {
            //             const user = res.user
            //             const avatar = (user.avatar == "" || user.avatar == null) ? defAva : import.meta.env.VITE_APP_BASE_API + user.avatar;
            //
            //             if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            //                 this.roles = res.roles
            //                 this.permissions = res.permissions
            //             } else {
            //                 this.roles = ['ROLE_DEFAULT']
            //             }
            //             this.name = user.userName
            //             this.avatar = avatar;
            //             resolve(res)
            //         }).catch(error => {
            //             reject(error)
            //         })
            //     })
            // },
            // // 退出系统
            logOut() {
                return new Promise((resolve, reject) => {
                    logout('0001',this.token).then(() => {
                        this.token = ''
                        this.roles = []
                        this.permissions = []
                        removeToken()
                        resolve()
                    }).catch(error => {
                        reject(error)
                    })
                })
            }
        }
    })

export default useUserStore
