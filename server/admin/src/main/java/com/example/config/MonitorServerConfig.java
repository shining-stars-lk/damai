package com.example.config;

import com.example.monitor.DingTalkMessage;
import com.example.monitor.MonitorServer;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-26
 **/
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
