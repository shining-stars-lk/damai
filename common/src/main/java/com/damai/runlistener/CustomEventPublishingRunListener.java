package com.damai.runlistener;

import com.damai.core.StringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 服务名配置
 * @author: 阿宽不是程序员
 **/
public class CustomEventPublishingRunListener implements SpringApplicationRunListener, Ordered {
    
    private static final String SPRING_APPLICATION_NAME = "spring.application.name";
    
    private final SpringApplication application;
    
    private final String[] args;
    
    public CustomEventPublishingRunListener(SpringApplication application, String[] args){
        this.application = application;
        this.args = args;
    }
    @Override
    public int getOrder() {
        return 1;
    }
    
    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        String applicationName = environment.getProperty(SPRING_APPLICATION_NAME);
        if (StringUtil.isNotEmpty(applicationName)) {
            System.setProperty("applicationName", applicationName);
        }
    }
}
