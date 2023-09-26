package com.example.service;


import com.example.model.ApiStatInvokedInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class ApiStatInvokedQueue {
    
    private static final BlockingQueue<ApiStatInvokedInfo> blockingQueue = new ArrayBlockingQueue<>(50000);

    public static void add(ApiStatInvokedInfo apiStatInvokedInfo) {
        blockingQueue.add(apiStatInvokedInfo);
    }
    

    public static void onInveked() {
        for (;;) {
            try {
                ApiStatInvokedInfo poll = blockingQueue.take();
            } catch (InterruptedException e) {
                log.error("blockingQueue.take error");
                Thread.currentThread().interrupt();
            }
        }
    }
}
