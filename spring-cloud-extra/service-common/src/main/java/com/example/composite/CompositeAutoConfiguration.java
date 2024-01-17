package com.example.composite;

import com.example.composite.event.ApplicationStartedEventHandler;
import org.springframework.context.annotation.Bean;

public class CompositeAutoConfiguration {
    
    @Bean
    public ApplicationStartedEventHandler applicationStartedEventHandler(CompositeContainer compositeContainer){
        return new ApplicationStartedEventHandler(compositeContainer);
    }
    
    @Bean
    public CompositeContainer compositeContainer(){
        return new CompositeContainer();
    }
}
