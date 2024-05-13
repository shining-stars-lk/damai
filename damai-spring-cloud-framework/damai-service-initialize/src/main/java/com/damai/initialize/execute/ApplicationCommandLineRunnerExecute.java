package com.damai.initialize.execute;

import com.damai.initialize.execute.base.AbstractApplicationExecute;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;

import static com.damai.initialize.constant.InitializeHandlerType.APPLICATION_COMMAND_LINE_RUNNER;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用于处理 {@link CommandLineRunner} 应用程序启动事件。
 * @author: 阿星不是程序员
 **/
public class ApplicationCommandLineRunnerExecute extends AbstractApplicationExecute implements CommandLineRunner {
    
    public ApplicationCommandLineRunnerExecute(ConfigurableApplicationContext applicationContext){
        super(applicationContext);
    }
    
    @Override
    public void run(final String... args) {
        execute();
    }
    
    @Override
    public String type() {
        return APPLICATION_COMMAND_LINE_RUNNER;
    }
}
