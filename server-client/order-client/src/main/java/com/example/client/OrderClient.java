package com.example.client;

import com.example.common.ApiResponse;
import com.example.dto.OrderCreateDto;
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
@FeignClient(value = "order-service",fallback = OrderClientFallback.class)
public interface OrderClient {
    
    @PostMapping("/order/create")
    ApiResponse<String> create(OrderCreateDto orderCreateDto);
}
