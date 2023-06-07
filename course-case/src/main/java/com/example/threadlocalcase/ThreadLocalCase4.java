package com.example.threadlocalcase;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: 
 * @description: 使用TransmittableThreadLocal来解决线程池线程复用的问题
 * @author: k
 * @create: 2023-04-19
 **/
public class ThreadLocalCase4 {
    
    private static ExecutorService executor = Executors.newFixedThreadPool(2);
    
    private static TransmittableThreadLocal<Integer> threadLocal = new TransmittableThreadLocal<>();
    
    public static void main(String[] args) {
        
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            int value = random.nextInt(10000);
            threadLocal.set(value);
            System.out.println(Thread.currentThread().getName() + "放入值，值为 : " + value);
            executor.execute(TtlRunnable.get(() -> {
                System.out.println(Thread.currentThread().getName() + "进行取值，值为 : " + threadLocal.get());
            }));
            System.out.println(Thread.currentThread().getName() + "进行取值，值为 : " + threadLocal.get());
            threadLocal.remove();
        }
        
    }
}
