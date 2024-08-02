import request from '@/utils/request'

/**
 * 购票人列表
 * @param params
 * */
export function selectTicketUserListApi(params){
    return request({
        url: '/damai/user/ticket/user/list',
        method: 'post',
        data: params
    })
}

/**
 * 删除购票人
 * @param params
 * */
export function delTicketUserApi(params){
    return request({
        url: '/damai/user/ticket/user/delete',
        method: 'post',
        data: params
    })
}