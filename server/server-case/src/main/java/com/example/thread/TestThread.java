package com.example.thread;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-08-16
 **/
public class TestThread implements Runnable{
    
    private String message;
    
    public TestThread(String message){
        this.message = message;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        RequestAttributes requestAttributes3 = RequestContextHolder.getRequestAttributes();
        if (requestAttributes3 != null) {
            ServletRequestAttributes servletRequestAttributes3 = (ServletRequestAttributes) requestAttributes3;
            HttpServletRequest request3 = servletRequestAttributes3.getRequest();
            String traceId3 = request3.getHeader("traceId");
            System.out.println(message + traceId3);
        }
    }
}
