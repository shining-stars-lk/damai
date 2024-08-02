import request from '@/utils/request'


//修改密码
export function getEditPsd(data){
    return request({
        url: '/damai/user/user/update/password',
        method: 'post',
        data:data
    })
}
// 修改邮箱
export function getEditEmail(data){
    return request({
        url: '/damai/user/user/update/email',
        method: 'post',
        data:data
    })
}
//修改手机号
export function getEditMobile(data){
    return request({
        url: '/damai/user/user/update/mobile',
        method: 'post',
        data:data
    })
}
//实名认证
export function getAuthentication(data){
    return request({
        url: '/damai/user/user/authentication',
        method: 'post',
        data:data
    })
}
