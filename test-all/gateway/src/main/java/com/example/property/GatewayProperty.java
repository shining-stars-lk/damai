package com.example.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-06-03
 **/
@Component
@Data
public class GatewayProperty {
    
    @Value("${api.limit.increment.value:1}")
    private String incrementValue;
    
    @Value("${api.limit.expiration.time:1}")
    private String expirationTime;
    
    @Value("${api.limit.semaphore:10}")
    private Long semaphore;
    //需要做频率限制的路径
    @Value("${api.limit.paths:#{null}}")
    private String[] apiLimitPaths;
}
