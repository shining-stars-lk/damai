package com.damai.pay.alipay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 支付宝支付相关配置
 * @author: 阿宽不是程序员
 **/
@Data
@ConfigurationProperties(prefix = AlipayProperties.PREFIX)
public class AlipayProperties {
    
    public static final String PREFIX = "alipay";
    
    private String appId;
    
    private String sellerId;
    
    private String gatewayUrl;
    
    private String merchantPrivateKey;
    
    private String alipayPublicKey;
    
    private String contentKey;
    
    private String returnUrl;
    
    private String notifyUrl;
}
