package com.damai.service;

import com.damai.client.BaseDataClient;
import com.damai.common.ApiResponse;
import com.damai.core.RedisKeyEnum;
import com.damai.dto.GetChannelDataByCodeDto;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyWrap;
import com.damai.vo.GetChannelDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-05-04
 **/
@Service
public class ChannelDataService {

    @Autowired
    private BaseDataClient baseDataClient;
    
    @Autowired
    private RedisCache redisCache;
    
    public GetChannelDataVo getChannelDataByCode(String code){
        GetChannelDataVo channelDataVo = getChannelDataByRedis(code);
        if (Objects.isNull(channelDataVo)) {
            channelDataVo = getChannelDataByClient(code);
        }
        return channelDataVo;
    }
    
    private GetChannelDataVo getChannelDataByRedis(String code){
        return redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.CHANNEL_DATA,code),GetChannelDataVo.class);
    }
    
    private GetChannelDataVo getChannelDataByClient(String code){
        GetChannelDataByCodeDto getChannelDataByCodeDto = new GetChannelDataByCodeDto();
        getChannelDataByCodeDto.setCode(code);
        ApiResponse<GetChannelDataVo> getChannelDataApiResponse = baseDataClient.getByCode(getChannelDataByCodeDto);
        if (Objects.equals(getChannelDataApiResponse.getCode(), BaseCode.SUCCESS.getCode())) {
            return getChannelDataApiResponse.getData();
        }
        throw new DaMaiFrameException("没有找到ChannelData");
    }
}
