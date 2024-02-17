package com.damai.config;

import com.damai.ApiStatThreadPool;
import com.damai.common.ApiStatCommon;
import com.damai.common.impl.ApiStatSingleServiceCommon;
import com.damai.event.ApiStatEventHandler;
import com.damai.event.ApiStatEventPush;
import com.damai.handler.ApiStatRunTimeHandler;
import com.damai.redis.RedisCache;
import com.damai.MethodDataStackHolder;
import com.damai.handler.MethodHierarchyTransferHandler;
import com.damai.operate.MethodDataOperate;
import com.damai.operate.MethodHierarchyTransferOperate;
import com.damai.operate.MethodQueueOperate;
import com.damai.save.DataSave;
import com.damai.save.impl.RedisDataSave;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@ConditionalOnProperty(value = "api-stat.enable",havingValue = "true")
public class ApiStatAutoConfig {

    @Bean
    public ApiStatThreadPool apiStatThreadPool(){
        return new ApiStatThreadPool();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiStatCommon ApiStatSingleServiceCommon(){
        return new ApiStatSingleServiceCommon();
    }
    
    @Bean
    public MethodDataStackHolder methodDataStackHolder(){
        return new MethodDataStackHolder();
    }
    @Bean
    public MethodDataOperate methodDataOperate(MethodDataStackHolder methodDataStackHolder,ApiStatCommon apiStatCommon){
        return new MethodDataOperate(methodDataStackHolder,apiStatCommon);
    }
    
    @Bean
    public MethodHierarchyTransferOperate methodHierarchyTransferOperate(){
        return new MethodHierarchyTransferOperate();
    }
    
    @Bean
    public MethodQueueOperate methodQueueOperate(MethodHierarchyTransferHandler methodHierarchyTransferHandler,ApiStatProperties apiStatProperties){
        return new MethodQueueOperate(methodHierarchyTransferHandler,apiStatProperties);
    }

    @Bean
    public DataSave dataSave(RedisCache redisCache,ApiStatProperties apiStatProperties,ApiStatCommon apiStatCommon){
        return new RedisDataSave(redisCache,apiStatProperties,apiStatCommon);
    }
    
    @Bean
    public MethodHierarchyTransferHandler methodHierarchyTransferHandler(DataSave dataSave){
        return new MethodHierarchyTransferHandler(dataSave);
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
    public ApiStatEventPush apiStatEventPush(){
        return new ApiStatEventPush();
    }

    @Bean
    public ApiStatEventHandler apiStatEventHandler(MethodQueueOperate methodQueueOperate){
        return new ApiStatEventHandler(methodQueueOperate);
    }
}
