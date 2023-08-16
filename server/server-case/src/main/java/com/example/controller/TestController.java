package com.example.controller;

import com.example.thread.TestThread;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-21
 **/
@RestController
@RequestMapping("/test")
public class TestController {
    
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,1,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(30));
    
    @PostMapping("test")
    public String test(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String traceId = request.getHeader("traceId");
        System.out.println("===同步获取 traceId:" + traceId);
        TestThread testThread = new TestThread("===异步获取 traceId2:");
        testThread.run();
//        threadPoolExecutor.execute(new TestThread("===异步获取 traceId2:"));
//        Thread thread = new Thread(new TestThread("===异步获取 traceId3:"));
//        thread.start();
        
//        Thread thread = new Thread(new TestThreadV2());
//        thread.start();
//        threadPoolExecutor.execute(() -> System.out.println("线程池执行"));
        return String.valueOf("true");
    }
}
