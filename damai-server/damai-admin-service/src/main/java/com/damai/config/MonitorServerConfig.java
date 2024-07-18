package com.damai.config;

import com.damai.monitor.DingTalkMessage;
import com.damai.monitor.MonitorServer;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 服务监控 配置
 * @author: 阿星不是程序员
 **/
@Configuration
public class MonitorServerConfig {
    
    @Value("${dingtalk.token:}")
    private String token;
    
    @Bean
    public DingTalkMessage dingTalkMessage(){
        return new DingTalkMessage(token);
    }
    
    @Bean
    public MonitorServer monitorServer(DingTalkMessage dingTalkMessage,InstanceRepository repository){
        return new MonitorServer(dingTalkMessage,repository);
    }
    
    
}
