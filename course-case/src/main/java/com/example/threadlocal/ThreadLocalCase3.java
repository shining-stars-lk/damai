package com.example.threadlocal;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: 
 * @description: 开启循环，每个循环中主线程使用InheritableThreadLocal进行设置，使用线程池来进行取值
 * @author: lk
 * @create: 2023-04-19
 **/
public class ThreadLocalCase3 {
    private static ExecutorService executor = Executors.newFixedThreadPool(2);
    private static InheritableThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            int value = random.nextInt(10000);
            threadLocal.set(value);
            System.out.println(Thread.currentThread().getName() + "放入值，值为 : " + value);
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "进行取值，值为 : " + threadLocal.get());
            });
            System.out.println(Thread.currentThread().getName() + "进行取值，值为 : " + threadLocal.get());
            threadLocal.remove();
        }
    }
}
