package com.example.service.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-25
 **/
@Data
@Component
public class OrderProperties {

    @Value("${orderPayNotifyUrl:localhost:8081}")
    private String orderPayNotifyUrl;
    
    @Value("${orderPayReturnUrl:localhost:8081}")
    private String orderPayReturnUrl;
}
