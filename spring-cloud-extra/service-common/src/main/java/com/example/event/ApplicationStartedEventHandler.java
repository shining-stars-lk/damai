package com.example.event;

import com.example.composite.CompositeContainer;
import com.example.init.InitDataContainer;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * ApplicationStartedEventHandler 类用于处理应用程序启动事件。
 */
@AllArgsConstructor
public class ApplicationStartedEventHandler implements ApplicationListener<ApplicationStartedEvent> {
    
    private final CompositeContainer compositeContainer;
    
    private final InitDataContainer initDataContainer;
    
    @Override
    public void onApplicationEvent(final ApplicationStartedEvent event) {
        compositeContainer.init(event.getApplicationContext());
        
        initDataContainer.initData(event.getApplicationContext());
    }
    
    
}
