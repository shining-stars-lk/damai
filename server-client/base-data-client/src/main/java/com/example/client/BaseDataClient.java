package com.example.client;

import com.example.common.Result;
import com.example.dto.GetChannelDataByCodeDto;
import com.example.vo.GetChannelDataVo;
import com.example.vo.TokenDataVo;
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
@FeignClient(value = "base-data-service",fallback = BaseDataClientFallback.class)
public interface BaseDataClient {
    
    @PostMapping("/channel/data/getByCode")
    Result<GetChannelDataVo> getByCode(GetChannelDataByCodeDto dto);
    
    @PostMapping (value = "/get")
    Result<TokenDataVo> get();
}
