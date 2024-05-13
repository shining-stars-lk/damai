package com.damai.initialize.execute.base;

import com.damai.initialize.base.InitializeHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Comparator;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用于处理应用程序启动执行的基类
 * @author: 阿星不是程序员
 **/
@AllArgsConstructor
public abstract class AbstractApplicationExecute {
    
    private final ConfigurableApplicationContext applicationContext;
    
    public void execute(){
        Map<String, InitializeHandler> initializeHandlerMap = applicationContext.getBeansOfType(InitializeHandler.class);
        initializeHandlerMap.values()
                .stream()
                .filter(initializeHandler -> initializeHandler.type().equals(type()))
                .sorted(Comparator.comparingInt(InitializeHandler::executeOrder))
                .forEach(initializeHandler -> {
                    initializeHandler.executeInit(applicationContext);
                });
    }
    /**
     * 初始化执行 类型
     * @return 类型
     * */
    public abstract String type();
}
