package com.damai.config;

import com.damai.core.PrefixDistinctionNameProperties;
import com.damai.core.SpringUtil;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 通用配置
 * @author: 阿宽不是程序员
 **/
@EnableConfigurationProperties(PrefixDistinctionNameProperties.class)
public class DaMaiCommonAutoConfig {
    
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer JacksonCustom(){
        return new JacksonCustom();
    }
    
    @Bean
    public SpringUtil springUtil(PrefixDistinctionNameProperties prefixDistinctionNameProperties){
        return new SpringUtil(prefixDistinctionNameProperties);
    }
    
}
