package com.damai.initialize.context;

import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.initialize.base.InitializeHandler;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 初始化执行策略上下文
 * @author: 阿宽不是程序员
 **/
public class InitializeContext implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    
    private Map<String, List<InitializeHandler>> map = new HashMap<>(8);
    
    
    public List<InitializeHandler> get(String type){
        return Optional.ofNullable(map.get(type)).orElseThrow(() -> new DaMaiFrameException(BaseCode.INITIALIZE_HANDLER_STRATEGY_NOT_EXIST));
    }
    
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Map<String, InitializeHandler> initializeHandlerMap = applicationContext.getBeansOfType(InitializeHandler.class);
        map = initializeHandlerMap.values().stream().collect(Collectors.groupingBy(InitializeHandler::type));
    }
}
