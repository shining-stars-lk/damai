package com.example.synchronizedcase;

/**
 * @program: toolkit
 * @description: 
 * 开启2个线程，一个线程中加上synchronized对象锁，循环50000次对result变量进行++自增，
 * 另一个线程加synchronized到此class上，循环50000次对result变量进行++自增，
 * 主线程等待10s后输出result值结果 
 * @author: 星哥
 * @create: 2023-04-23
 **/
public class SynchronizedCase6 {
    private static Integer result = 0;
    
    public static void main(String[] args) {
        SynchronizedCase6 synchronizedCase6 = new SynchronizedCase6();
        synchronizedCase6.test();
    }
    
    public void test(){
        new Thread(() -> {
            synchronized (this) {
                for (int j = 0; j < 50000; j++) {
                    result++;
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (SynchronizedCase6.class) {
                for (int j = 0; j < 50000; j++) {
                    result++;
                }
            }
        }).start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("执行结果 result : " + result);
    }
}
