package com.example.lambda;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-09
 **/
public class Case1 {
    
    public static void main(String[] args) {
        TestRun testRun1 = new TestRun() {
            @Override
            public void run() {
                System.out.println("testRun方法执行");      
            }
        };
       testRun1.run();
        
        
        TestRun testRun2 = () -> {
            System.out.println("testRun方法执行");
        };
        testRun2.run();
        
        TestRun testRun3 = () -> System.out.println("testRun方法执行");
        testRun3.run();
    }
}
