package com.example.balance;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@ConfigurationProperties("spring.cloud.nacos.discovery.metadata")
@Data
public class ExtraRibbonProperties {
    
    private String mark;
}
