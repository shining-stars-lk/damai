package com.damai.initialize.config;

import com.damai.initialize.context.InitializeContext;
import com.damai.initialize.execute.ApplicationInitializingBeanExecute;
import com.damai.initialize.execute.ApplicationPostConstructExecute;
import com.damai.initialize.execute.ApplicationStartEventListenerExecute;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 初始化执行 相关配置
 * @author: 阿宽不是程序员
 **/
public class InitializeAutoConfig {
    
    @Bean
    public InitializeContext initializeContext(){
        return new InitializeContext();
    }
    
    @Bean
    public ApplicationInitializingBeanExecute applicationInitializingBeanExecute(
            ConfigurableApplicationContext applicationContext,
            InitializeContext initializeContext){
        return new ApplicationInitializingBeanExecute(applicationContext,initializeContext);
    }
    
    @Bean
    public ApplicationPostConstructExecute applicationPostConstructExecute(
            ConfigurableApplicationContext applicationContext,
            InitializeContext initializeContext){
        return new ApplicationPostConstructExecute(applicationContext,initializeContext);
    }
    
    @Bean
    public ApplicationStartEventListenerExecute applicationStartEventListenerExecute(
            InitializeContext initializeContext){
        return new ApplicationStartEventListenerExecute(initializeContext);
    }
}
