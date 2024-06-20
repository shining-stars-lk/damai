package com.damai.pay.alipay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 支付宝支付相关配置
 * @author: 阿星不是程序员
 **/
@Data
@ConfigurationProperties(prefix = AlipayProperties.PREFIX)
public class AlipayProperties {
    
    public static final String PREFIX = "alipay";
    
    /**
     * 应用ID
     * */
    private String appId;
    
    /**
     * 商户PID
     * */
    private String sellerId;
    
    /**
     * 支付宝网关
     * */
    private String gatewayUrl;
    
    /**
     * 商户私钥，您的PKCS8格式RSA2私钥
     * */
    private String merchantPrivateKey;
    
    /**
     * 支付宝公钥,查看地址：<a href="https://openhome.alipay.com/platform/keyManage.htm"/> 对应APPID下的支付宝公钥
     * */
    private String alipayPublicKey;
    
    /**
     * 接口内容加密秘钥，对称秘钥
     * */
    private String contentKey;
    
    /**
     * 页面跳转同步通知页面路径
     * */
    private String returnUrl;
    
    /**
     * 支付宝异步回调接口  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * */
    private String notifyUrl;
}
