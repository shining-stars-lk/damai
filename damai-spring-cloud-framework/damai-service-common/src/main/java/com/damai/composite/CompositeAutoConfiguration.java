package com.damai.composite;

import com.damai.event.ApplicationStartedEventHandler;
import com.damai.init.InitDataContainer;
import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 组合模式配置
 * @author: 阿宽不是程序员
 **/
public class CompositeAutoConfiguration {
    
    @Bean
    public ApplicationStartedEventHandler applicationStartedEventHandler(CompositeContainer compositeContainer, InitDataContainer initDataContainer){
        return new ApplicationStartedEventHandler(compositeContainer,initDataContainer);
    }
    
    @Bean
    public CompositeContainer compositeContainer(){
        return new CompositeContainer();
    }
}
