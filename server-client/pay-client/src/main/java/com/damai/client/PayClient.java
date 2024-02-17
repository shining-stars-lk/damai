package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.NotifyDto;
import com.damai.dto.PayDto;
import com.damai.dto.TradeCheckDto;
import com.damai.vo.NotifyVo;
import com.damai.vo.TradeCheckVo;
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
    
    @PostMapping(value = "/pay/notify")
    ApiResponse<NotifyVo> notify(NotifyDto notifyDto);
    
    @PostMapping(value = "/pay/trade/check")
    ApiResponse<TradeCheckVo> tradeCheck(TradeCheckDto tradeCheckDto);
}
