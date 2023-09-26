package com.example.config;

import com.example.handler.ApiStatRunTimeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-26
 **/
@Slf4j
@EnableConfigurationProperties(ApiStatProperties.class)
@ConditionalOnProperty(havingValue = "api-stat.pointcut")
public class ApiStatAutoConfig {
    
    @Bean
    public AspectJExpressionPointcutAdvisor configurabledvisor(ApiStatProperties apiStatProperties) {
        log.info("api-stat=>loading method listener");
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        String cutRange = apiStatProperties.getPointcut();
        cutRange = cutRange + " && !@annotation(cn.langpy.kotime.annotation.KoListener)";
        advisor.setExpression(cutRange);
        advisor.setAdvice(new ApiStatRunTimeHandler());
        return advisor;
    }
}
