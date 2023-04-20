package com.example.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * @program: 开启5个线程，主线程执行await()进行阻塞，每个线程执行countDown()，当达到CountDownLatch初始化时设置的次数后
 *           主线程会继续执行
 * @description: 
 * @author: lk
 * @create: 2023-04-20
 **/
public class CountDownLatchCase1 {
    
    public static void main(String[] args) {
        
        CountDownLatch countDownLatch = new CountDownLatch(5);
        long mainThreadStartTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "开始执行 ");
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                long threadStartTime = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName() + "开始执行 ");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                countDownLatch.countDown();
                System.out.println(Thread.currentThread().getName() + "结束执行 耗时 : " + (System.currentTimeMillis() - threadStartTime));
            });
            thread.setName("thread-" + i);
            thread.start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(Thread.currentThread().getName() + "结束执行 耗时 : " + (System.currentTimeMillis() - mainThreadStartTime));
    }
}
