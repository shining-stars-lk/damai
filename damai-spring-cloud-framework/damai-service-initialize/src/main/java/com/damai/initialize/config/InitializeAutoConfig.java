package com.damai.initialize.config;

import com.damai.initialize.execute.ApplicationCommandLineRunnerExecute;
import com.damai.initialize.execute.ApplicationInitializingBeanExecute;
import com.damai.initialize.execute.ApplicationPostConstructExecute;
import com.damai.initialize.execute.ApplicationStartEventListenerExecute;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 初始化执行 相关配置
 * @author: 阿星不是程序员
 **/
public class InitializeAutoConfig {
    
    @Bean
    public ApplicationInitializingBeanExecute applicationInitializingBeanExecute(
            ConfigurableApplicationContext applicationContext){
        return new ApplicationInitializingBeanExecute(applicationContext);
    }
    
    @Bean
    public ApplicationPostConstructExecute applicationPostConstructExecute(
            ConfigurableApplicationContext applicationContext){
        return new ApplicationPostConstructExecute(applicationContext);
    }
    
    @Bean
    public ApplicationStartEventListenerExecute applicationStartEventListenerExecute(
            ConfigurableApplicationContext applicationContext){
        return new ApplicationStartEventListenerExecute(applicationContext);
    }
    
    @Bean
    public ApplicationCommandLineRunnerExecute applicationCommandLineRunnerExecute(
            ConfigurableApplicationContext applicationContext){
        return new ApplicationCommandLineRunnerExecute(applicationContext);
    }
}
