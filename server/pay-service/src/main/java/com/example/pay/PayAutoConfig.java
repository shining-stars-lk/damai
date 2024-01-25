package com.example.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.example.pay.alipay.AlipayStrategyHandler;
import com.example.pay.alipay.config.AlipayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-25
 **/

@EnableConfigurationProperties(AlipayProperties.class)
public class PayAutoConfig {
    
    @Bean
    public AlipayClient alipayClient(AlipayProperties aliPayProperties) throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(aliPayProperties.getGatewayUrl());
        alipayConfig.setAppId(aliPayProperties.getAppId());
        alipayConfig.setPrivateKey(aliPayProperties.getMerchantPrivateKey());
        alipayConfig.setFormat(AlipayConstants.FORMAT_JSON);
        alipayConfig.setCharset(AlipayConstants.CHARSET_UTF8);
        alipayConfig.setAlipayPublicKey(aliPayProperties.getAlipayPublicKey());
        alipayConfig.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
        //构造client
        return new DefaultAlipayClient(alipayConfig);
    }
    
    @Bean
    public PayStrategyContext payStrategyContext(){
        return new PayStrategyContext();
    }
    
    @Bean
    public PayStrategyInitHandler payStrategyInitHandler(PayStrategyContext payStrategyContext){
        return new PayStrategyInitHandler(payStrategyContext);
    }
    
    @Bean
    public AlipayStrategyHandler alipayCall(AlipayClient alipayClient, AlipayProperties aliPayProperties){
        return new AlipayStrategyHandler(alipayClient,aliPayProperties);
    }
}
