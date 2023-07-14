package com.example.client;

import com.example.dto.GetDto;
import com.example.vo.GetVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-04-17
 **/
@Component
@FeignClient(value = "product-service",fallback = ProductClientFallback.class)
public interface ProductClient {
    
    @PostMapping("/product/get")
    GetVo get(GetDto dto);
    
    @PostMapping("/product/getV2")
    GetVo getV2(GetDto dto);
}
