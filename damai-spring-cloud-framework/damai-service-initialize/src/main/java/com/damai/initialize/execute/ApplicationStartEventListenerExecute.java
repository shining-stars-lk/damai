package com.damai.initialize.execute;

import com.damai.initialize.base.InitializeHandler;
import com.damai.initialize.context.InitializeContext;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Comparator;
import java.util.List;

import static com.damai.initialize.constant.InitializeHandlerType.APPLICATION_START_EVENT_LISTENER;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 用于处理 {@link ApplicationStartedEvent} 应用程序启动事件。
 * @author: 阿宽不是程序员
 **/
@AllArgsConstructor
public class ApplicationStartEventListenerExecute implements ApplicationListener<ApplicationStartedEvent> {
    
    private final InitializeContext initializeContext;
    
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        List<InitializeHandler> initializeHandlers = initializeContext.get(APPLICATION_START_EVENT_LISTENER);
        initializeHandlers.stream().sorted(Comparator.comparingInt(InitializeHandler::executeOrder))
                .forEach(initializeHandler -> {
                    initializeHandler.executeInit(event.getApplicationContext());
                });
    }
}
