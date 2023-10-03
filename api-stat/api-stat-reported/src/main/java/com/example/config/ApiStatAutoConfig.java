package com.example.config;

import com.example.ApiStatThreadPool;
import com.example.common.ApiStatCommon;
import com.example.common.impl.ApiStatSingleServiceCommon;
import com.example.event.ApiStatEventHandler;
import com.example.event.ApiStatEventPush;
import com.example.handler.ApiStatRunTimeHandler;
import com.example.redis.RedisCache;
import com.example.MethodDataStackHolder;
import com.example.handler.MethodHierarchyTransferHandler;
import com.example.operate.MethodDataOperate;
import com.example.operate.MethodHierarchyTransferOperate;
import com.example.operate.MethodQueueOperate;
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
    public ApiStatThreadPool apiStatThreadPool(){
        return new ApiStatThreadPool();
    }
    
    @Bean
    public MethodDataStackHolder methodDataStackHolder(){
        return new MethodDataStackHolder();
    }
    @Bean
    public MethodDataOperate methodDataOperate(MethodDataStackHolder methodDataStackHolder){
        return new MethodDataOperate(methodDataStackHolder);
    }
    
    @Bean
    public MethodHierarchyTransferOperate methodHierarchyTransferOperate(){
        return new MethodHierarchyTransferOperate();
    }
    
    @Bean
    public MethodQueueOperate methodQueueOperate(MethodHierarchyTransferHandler methodHierarchyTransferHandler){
        return new MethodQueueOperate(methodHierarchyTransferHandler);
    }
    
    @Bean
    public MethodHierarchyTransferHandler methodHierarchyTransferHandler(RedisCache redisCache){
        return new MethodHierarchyTransferHandler(redisCache);
    }
    
    @Bean
    public AspectJExpressionPointcutAdvisor apiStatAdvisor(ApiStatProperties apiStatProperties, MethodDataOperate methodDataOperate,
                                                           MethodDataStackHolder methodDataStackHolder,
                                                           MethodHierarchyTransferOperate methodHierarchyTransferOperate,
                                                           MethodQueueOperate methodQueueOperate) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(apiStatProperties.getPointcut());
        advisor.setAdvice(new ApiStatRunTimeHandler(methodDataOperate,methodDataStackHolder,
                methodHierarchyTransferOperate,methodQueueOperate));
        return advisor;
    }

    @Bean
    public ApiStatCommon ApiStatSingleServiceCommon(){
        return new ApiStatSingleServiceCommon();
    }

    @Bean
    public ApiStatEventPush apiStatEventPush(){
        return new ApiStatEventPush();
    }

    @Bean
    public ApiStatEventHandler apiStatEventHandler(MethodQueueOperate methodQueueOperate){
        return new ApiStatEventHandler(methodQueueOperate);
    }
}
