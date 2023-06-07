package com.example.findbean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-05-23
 **/
@Configuration
public class FindBeanConfig {
    
    @Bean
    public ApplicationContextHolder applicationContextHolder(){
        return new ApplicationContextHolder();
    }
    
    @Bean("baseBean")
    public Base aBaseBean(){
        return new ABaseBean();
    }
    
    @Bean("cBaseBean")
    @ConditionalOnMissingBean(Base.class)
    public Base cBaseBean(){
        return new CBaseBean();
    }
    
    
}
