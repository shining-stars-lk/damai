import request from '@/utils/request'
 /**
 *登录接口

 * @param password 密码
 * @param code 渠道code 0001(pc网站)
 * @param email   用户邮箱(手机号任选其一)
 * @param mobile    用户手机号(邮箱任选其一)
 * @returns {*}
 */
export function login(email, mobile, password, code) {
    const data = {
        email,
        mobile,
        password,
        code
    }
    return request({
        url: '/damai/user/user/login',
        method: 'post',
        data: data
    })
}

 /**
  * 退出接口
  * @param code
  * @param token
  * @returns {*}
  */
 export function logout(code,token) {
     const data = {
         token,
         code
     }
     return request({
         url: '/damai/user/user/logout',
         method: 'post',
         data:data
     })
 }

 /**
  * 检查是否需要验证码
  * @returns {*}
  */
 export function isCaptcha(){
     return request({
         url: '/damai/user/user/captcha/check/need',
         method: 'post'
     })
 }



 export function register(data){
     return request({
         url: '/damai/user/user/register',
         method: 'post',
         data:data
     })
 }
