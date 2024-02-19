package com.damai.filter;

import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 过滤器配置
 * @author: 阿宽不是程序员
 **/

public class FilterAutoConfiguration {
    
    @Bean
    public BaseParameterFilter baseParameterFilter(){
        return new BaseParameterFilter();
    }
}
