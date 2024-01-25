package com.example.client;

import com.example.common.ApiResponse;
import com.example.dto.PayDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @program: cook-frame
 * @description:
 * @author: lk
 * @create: 2024-01-25
 **/
@Component
@FeignClient(value = "pay-service",fallback = PayClientFallback.class)
public interface PayClient {
    
    @PostMapping(value = "/pay/common/pay")
    ApiResponse<String> commonPay(PayDto payDto);
}
