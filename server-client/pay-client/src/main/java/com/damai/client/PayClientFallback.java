package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.NotifyDto;
import com.damai.dto.PayDto;
import com.damai.dto.TradeCheckDto;
import com.damai.enums.BaseCode;
import com.damai.vo.NotifyVo;
import com.damai.vo.TradeCheckVo;
import org.springframework.stereotype.Component;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-25
 **/
@Component
public class PayClientFallback implements PayClient{
    
    @Override
    public ApiResponse<String> commonPay(final PayDto payDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
    
    @Override
    public ApiResponse<NotifyVo> notify(final NotifyDto notifyDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
    
    @Override
    public ApiResponse<TradeCheckVo> tradeCheck(final TradeCheckDto tradeCheckDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
