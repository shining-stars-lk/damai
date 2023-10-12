package com.example.client;

import com.example.common.ApiResponse;
import com.example.dto.InfoDto;
import com.example.vo.InfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Component
@FeignClient(value = "provider-service",fallback = ProviderClientFallback.class)
public interface ProviderClient {
    
    @PostMapping("/provider/getInfo")
    ApiResponse<InfoVo> getInfo(InfoDto infoDto);
}
