package com.example.pay;

import com.example.entity.PayBill;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-25
 **/
public interface PayStrategyHandler {
    
    PayResult pay(String outTradeNo, BigDecimal price, String subject, String notifyUrl, String returnUrl);
    
    boolean signVerify(Map<String, String> params);
    
    boolean dataVerify(Map<String, String> params, PayBill payBill);
    
    TradeResult queryTrade(String outTradeNo);
    
    String getChannel();
}
