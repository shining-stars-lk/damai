package com.example.config;

import com.example.operate.MethodQueueOperate;
import com.example.service.ApiStatInvokedQueue;
import lombok.AllArgsConstructor;

import javax.annotation.PostConstruct;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-27
 **/
@AllArgsConstructor
public class ApiStatInit {

    private final MethodQueueOperate methodQueueOperate;
    @PostConstruct
    public void init()  {
        new Thread(methodQueueOperate::takeTask).start();
    }
    
    
}
