package com.example.client;

import com.example.common.ApiResponse;
import com.example.dto.PayDto;
import com.example.dto.TradeCheckDto;
import com.example.vo.TradeCheckVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

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
    
    @PostMapping(value = "/pay/alipay/notify")
    ApiResponse<String> alipayNotify(Map<String, String> params);
    
    @PostMapping(value = "/pay/trade/check")
    ApiResponse<TradeCheckVo> tradeCheck(TradeCheckDto tradeCheckDto);
}
