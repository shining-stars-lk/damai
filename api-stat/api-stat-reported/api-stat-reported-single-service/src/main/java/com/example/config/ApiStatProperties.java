package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-27
 **/
@Data
@ConfigurationProperties(prefix = ApiStatProperties.prefix)
public class ApiStatProperties {

    public static final String prefix = "api-stat";
    
    private boolean enable;
    
    private String pointcut;
    
    private Integer queueSize = 5000;

    private Map<String,String[]> noReported;
}
