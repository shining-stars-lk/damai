package com.example.composite;

import com.example.event.ApplicationStartedEventHandler;
import com.example.init.InitDataContainer;
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
