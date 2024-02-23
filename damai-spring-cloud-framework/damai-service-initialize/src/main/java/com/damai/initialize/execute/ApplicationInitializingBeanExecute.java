package com.damai.initialize.execute;

import com.damai.initialize.base.InitializeHandler;
import com.damai.initialize.context.InitializeContext;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Comparator;
import java.util.List;

import static com.damai.initialize.constant.InitializeHandlerType.APPLICATION_START_INITIALIZING_BEAN;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 用于处理 {@link InitializingBean} 应用程序启动事件。
 * @author: 阿宽不是程序员
 **/
@AllArgsConstructor
public class ApplicationInitializingBeanExecute implements InitializingBean {
    
    private final ConfigurableApplicationContext applicationContext;
    
    private final InitializeContext initializeContext;
    
    @Override
    public void afterPropertiesSet() {
        List<InitializeHandler> initializeHandlers = initializeContext.get(APPLICATION_START_INITIALIZING_BEAN);
        initializeHandlers.stream().sorted(Comparator.comparingInt(InitializeHandler::executeOrder))
                .forEach(initializeHandler -> {
                    initializeHandler.executeInit(applicationContext);
                });
    }
}
