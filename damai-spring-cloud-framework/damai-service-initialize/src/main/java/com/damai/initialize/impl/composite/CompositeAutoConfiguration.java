package com.damai.initialize.impl.composite;

import com.damai.initialize.impl.composite.init.CompositeInit;
import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 组合模式配置
 * @author: 阿星不是程序员
 **/
public class CompositeAutoConfiguration {
    
    @Bean
    public CompositeContainer compositeContainer(){
        return new CompositeContainer();
    }
    
    @Bean
    public CompositeInit compositeInit(CompositeContainer compositeContainer){
        return new CompositeInit(compositeContainer);
    }
}
