package com.example.service;


import com.example.model.ApiStatInvokedInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class ApiStatInvokedQueue {

    private final BlockingQueue<ApiStatInvokedInfo> blockingQueue = new ArrayBlockingQueue<>(5000);
    
    private final ApiStatInvokedHandler apiStatInvokedHandler;
    
    public ApiStatInvokedQueue(ApiStatInvokedHandler apiStatInvokedHandler){
        this.apiStatInvokedHandler = apiStatInvokedHandler;
    }

    public void add(ApiStatInvokedInfo apiStatInvokedInfo) {
        blockingQueue.add(apiStatInvokedInfo);
    }
    

    public void onInveked() {
        for(;;) {
            try {
                ApiStatInvokedInfo apiStatInvokedInfo = blockingQueue.take();
                apiStatInvokedHandler.onInvoked(apiStatInvokedInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
