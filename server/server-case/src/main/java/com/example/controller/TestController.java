package com.example.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-07-21
 **/
@RestController
@RequestMapping("/test")
public class TestController {
    
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,1,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(30));
    
    @PostMapping("test")
    public String test(){
        threadPoolExecutor.execute(() -> {
            System.out.println("22222");
        });
        return String.valueOf(2222);
    }
}
