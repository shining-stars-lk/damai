import request from '@/utils/request'

export function getProgramDetials(data) {
    return request({
        url: '/damai/program/program/detail',
        method: 'post',
        data:data

    })
}

