import request from '@/utils/request'

/**
 * 通过类型查询节目类型
 * @param data
 * @returns {*}
 */
export function getcategoryType(data) {
    return request({
        url: '/damai/program/program/category/selectByType',
        method: 'post',
        data: data
    })
}
export function getMainCategory(data) {
    return request({
        url: '/damai/program/program/home/list',
        method: 'post',
        data: data
    })
}
