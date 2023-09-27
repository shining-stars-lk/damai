package com.example.config;

import com.example.data.ApiStatMemoryBase;
import com.example.handler.ApiStatRunTimeHandler;
import com.example.service.ApiStatInvokedHandler;
import com.example.service.ApiStatInvokedQueue;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-27
 **/
@Slf4j
@Data
@EnableConfigurationProperties(ApiStatProperties.class)
@ConditionalOnProperty(value = "api-stat.enable")
public class ApiStatAutoConfig {
    
    @Bean
    public AspectJExpressionPointcutAdvisor apiStatAdvisor(ApiStatProperties apiStatProperties,ApiStatInvokedQueue apiStatInvokedQueue) {
        log.info("api stat load");
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(apiStatProperties.getPointcut());
        advisor.setAdvice(new ApiStatRunTimeHandler(apiStatProperties,apiStatInvokedQueue));
        return advisor;
    }
    
    @Bean
    public ApiStatMemoryBase apiStatMemoryBase(){
        return new ApiStatMemoryBase();
    }
    
    @Bean
    public ApiStatInvokedHandler apiStatInvokedHandler(ApiStatMemoryBase apiStatMemoryBase){
        return new ApiStatInvokedHandler(apiStatMemoryBase);
    }
    
    @Bean
    public ApiStatInvokedQueue apiStatInvokedQueue(ApiStatInvokedHandler apiStatInvokedHandler){
        return new ApiStatInvokedQueue(apiStatInvokedHandler);
    }
    
    @Bean
    public ApiStatInit apiStatInit(ApiStatInvokedQueue apiStatInvokedQueue){
        return new ApiStatInit(apiStatInvokedQueue);
    }
    
}
