import request from '@/utils/request'

//获取当前城市
export function getCurrentCity() {
    return request({
        url: '/damai/basedata/area/current',
        method: 'post',

    })
}

//获取热门城市
export function getHotCity() {
    return request({
        url: '/damai/basedata/area/hot',
        method: 'post',

    })
}

//获取其他城市
export function getOtherCity() {
    return request({
        url: '/damai/basedata/area/selectCityData',
        method: 'post',

    })
}

export function getCityInfo(data) {
    return request({
        url: '/damai/basedata/area/getById',
        method: 'post',
        data: data
    })
}
