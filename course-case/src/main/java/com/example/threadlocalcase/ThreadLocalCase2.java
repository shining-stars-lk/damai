package com.example.threadlocalcase;

import java.util.Random;

/**
 * @program: 
 * @description: 主线程向ThreadLocal(或者InheritableThreadLocal)设置值，子线程再取值
 * @author: 星哥
 * @create: 2023-04-19
 **/
public class ThreadLocalCase2 {
    
    //private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    
    private static InheritableThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();
    
    public static void main(String[] args) {
        Random random = new Random();
        int value = random.nextInt(10000);
        threadLocal.set(value);
        System.out.println(Thread.currentThread().getName() + "放入值，值为 : " + value);
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "进行取值，值为 : " + threadLocal.get());
        });
        thread.setName("thread-1");
        thread.start();
        System.out.println(Thread.currentThread().getName() + "进行取值，值为 : " + threadLocal.get());
    }
}
