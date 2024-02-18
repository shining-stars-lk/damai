package com.damai.event;

import com.damai.composite.CompositeContainer;
import com.damai.init.InitDataContainer;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: ApplicationStartedEventHandler 类用于处理应用程序启动事件。
 * @author: 阿宽不是程序员
 **/
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
