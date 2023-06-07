package com.example.synchronizedcase;

/**
 * @program: toolkit
 * @description: 开启10个线程，每个线程中循环100次对result变量进行++自增，主线程等待10s后输出result值结果
 *               将synchronized添加实例对象的方法上，锁的范围为此实例对象
 * @author: k
 * @create: 2023-04-23
 **/
public class SynchronizedCase3 {
    private static Integer result = 0;
    
    public void increment(){
        for (int j = 0; j < 100; j++) {
            result++;
        }
    }
    
    public static void main(String[] args) {
        SynchronizedCase3 synchronizedCase3 = new SynchronizedCase3();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                synchronizedCase3.increment();
            }).start();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("执行结果 result : " + result);
    }
}
