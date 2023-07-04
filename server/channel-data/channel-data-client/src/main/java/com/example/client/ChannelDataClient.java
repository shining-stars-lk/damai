package com.example.client;

import com.example.common.Result;
import com.example.dto.GetChannelDataByCodeDto;
import com.example.vo.GetChannelDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Component
@FeignClient(value = "channel-data-service",fallback = ChannelDataClientFallback.class)
public interface ChannelDataClient {
    
    @PostMapping("/channel/data/getByCode")
    Result<GetChannelDataVo> getByCode(GetChannelDataByCodeDto dto);
}
