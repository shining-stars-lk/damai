package com.damai.service;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import com.damai.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-12-03
 **/
@Slf4j
@Service
public class TestService {
    
    private Executor executor = Executors.newFixedThreadPool(2);
    
    ThreadLocal<String> tl = new ThreadLocal<>();
    
    InheritableThreadLocal<String> itl = new TransmittableThreadLocal<>();
    
    TransmittableThreadLocal<String> ttl = new TransmittableThreadLocal<>();
    
    TransmittableThreadLocal<String> ttl2 = new TransmittableThreadLocal<>() {
        @Override
        protected void beforeExecute() {
            String traceId = get();
            if (StringUtil.isNotEmpty(traceId)) {
                MDC.put("traceId",traceId);   
            }
        }
        
        @Override
        protected void afterExecute() {
            MDC.clear();
        }
    };
    
    public void testData(HttpServletRequest request){
        String traceId = request.getHeader("traceId");
        System.out.print("主线程traceId:"+traceId+"---");
        tl.set(traceId);
        new Thread(() -> {
            String traceId2 = tl.get();
            System.out.println("子线程traceId:"+traceId2);
        }).start();
    }
    
    public void testData2(HttpServletRequest request){
        String traceId = request.getHeader("traceId");
        System.out.print("主线程traceId:"+traceId+"---");
        itl.set(traceId);
        new Thread(() -> {
            String traceId2 = itl.get();
            System.out.println("子线程traceId:"+traceId2);
        }).start();
    }
    
    public void testData3(HttpServletRequest request){
        String traceId = request.getHeader("traceId");
        System.out.print("主线程traceId:"+traceId+"---");
        itl.set(traceId);
        executor.execute(() -> {
            String traceId2 = itl.get();
            System.out.println("子线程traceId:"+traceId2);
        });
    }
    
    public void testData4(HttpServletRequest request){
        String traceId = request.getHeader("traceId");
        System.out.print("主线程traceId:"+traceId+"---");
        ttl.set(traceId);
        executor.execute(TtlRunnable.get(() -> {
            String traceId2 = ttl.get();
            System.out.println("子线程traceId:"+traceId2);
        }));
    }
    
    public void testData5(HttpServletRequest request){
        String traceId = request.getHeader("traceId");
        System.out.print("主线程traceId:"+traceId+"---");
        ttl2.set(traceId);
        executor.execute(TtlRunnable.get(() -> {
            String traceId2 = MDC.get("traceId");
            System.out.println("子线程traceId:"+traceId2);
            log.info("测试日志输出链路id");
        }));
    }
}
