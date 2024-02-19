package com.damai.pay;

import com.damai.entity.PayBill;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 支付方式 策略抽象
 * @author: 阿宽不是程序员
 **/
public interface PayStrategyHandler {
    
    PayResult pay(String outTradeNo, BigDecimal price, String subject, String notifyUrl, String returnUrl);
    
    boolean signVerify(Map<String, String> params);
    
    boolean dataVerify(Map<String, String> params, PayBill payBill);
    
    TradeResult queryTrade(String outTradeNo);
    
    String getChannel();
}
