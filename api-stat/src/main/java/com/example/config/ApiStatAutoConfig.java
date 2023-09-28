package com.example.config;

import com.example.data.ApiStatMemoryBase;
import com.example.handler.ApiStatRunTimeHandler;
import com.example.rel.MethodDataStackHolder;
import com.example.rel.handler.MethodHierarchyTransferHandler;
import com.example.rel.operate.MethodDataOperate;
import com.example.rel.operate.MethodHierarchyTransferOperate;
import com.example.rel.operate.MethodQueueOperate;
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
    public MethodDataStackHolder methodDataStackHolder(){
        return new MethodDataStackHolder();
    }
    @Bean
    public MethodDataOperate methodDataOperate(MethodDataStackHolder methodDataStackHolder){
        return new MethodDataOperate(methodDataStackHolder);
    }
    
    @Bean
    public MethodHierarchyTransferOperate methodHierarchyTransferOperate(MethodDataOperate methodDataOperate){
        return new MethodHierarchyTransferOperate(methodDataOperate);
    }
    
    @Bean
    public MethodQueueOperate methodQueueOperate(MethodHierarchyTransferHandler methodHierarchyTransferHandler){
        return new MethodQueueOperate(methodHierarchyTransferHandler);
    }
    
    @Bean
    public MethodHierarchyTransferHandler methodHierarchyTransferHandler(){
        return new MethodHierarchyTransferHandler();
    }
    
    @Bean
    public AspectJExpressionPointcutAdvisor apiStatAdvisor(ApiStatProperties apiStatProperties,ApiStatInvokedQueue apiStatInvokedQueue,
                                                           MethodDataOperate methodDataOperate,MethodDataStackHolder methodDataStackHolder,
                                                           MethodHierarchyTransferOperate methodHierarchyTransferOperate,
                                                           MethodQueueOperate methodQueueOperate) {
        log.info("api stat load");
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(apiStatProperties.getPointcut());
        advisor.setAdvice(new ApiStatRunTimeHandler(apiStatProperties,apiStatInvokedQueue,methodDataOperate,methodDataStackHolder,
                methodHierarchyTransferOperate,methodQueueOperate));
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
    public ApiStatInit apiStatInit(ApiStatInvokedQueue apiStatInvokedQueue,MethodQueueOperate methodQueueOperate){
        return new ApiStatInit(apiStatInvokedQueue,methodQueueOperate);
    }
    
}
