package com.damai.pay;

import com.damai.entity.PayBill;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 支付方式 策略抽象
 * @author: 阿星不是程序员
 **/
public interface PayStrategyHandler {
    /**
     * 支付
     * @param outTradeNo 订单号
     * @param price 支付价格
     * @param subject 标题
     * @param notifyUrl 回调地址
     * @param returnUrl 支付后返回地址
     * @return 结果
     * */
    PayResult pay(String outTradeNo, BigDecimal price, String subject, String notifyUrl, String returnUrl);
    
    /**
     * 验签
     * @param params 参数
     * @return 结果
     * */
    boolean signVerify(Map<String, String> params);
    
    /**
     * 数据验证
     * @param params 参数
     * @param payBill 支付账单
     * @return 结果
     * */
    boolean dataVerify(Map<String, String> params, PayBill payBill);
    
    /**
     * 状态查询
     * @param outTradeNo 订单号
     * @return 结果
     * */
    TradeResult queryTrade(String outTradeNo);
    
    /**
     * 退款
     * @param outTradeNo 订单号
     * @param price 支付价格
     * @param reason 原因
     * @return 结果
     * */
    RefundResult refund(String outTradeNo, BigDecimal price, String reason);
    
    /**
     * 支付渠道
     * @return 结果
     * */
    String getChannel();
}
