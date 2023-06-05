package com.example.client;

import com.example.common.Result;
import com.example.vo.GetChannelDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Component
@FeignClient(value = "channel-data-client-service",fallback = ChannelDataClientFallback.class)
public interface ChannelDataClient {
    
    @GetMapping("/channel/data/getByCode")
    Result<GetChannelDataVo> getByCode(String code);
}
