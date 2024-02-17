package com.damai.composite;

import com.damai.event.ApplicationStartedEventHandler;
import com.damai.init.InitDataContainer;
import org.springframework.context.annotation.Bean;

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
