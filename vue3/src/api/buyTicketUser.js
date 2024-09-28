import request from '@/utils/request'

//查询
export function getTicketUser(data) {
    return request({
        url: '/damai/user/ticket/user/list',
        method: 'post',
        data:data
    })
}
//新增
export function saveTicketUser(data) {
    return request({
        url: '/damai/user/ticket/user/add',
        method: 'post',
        data:data
    })
}
