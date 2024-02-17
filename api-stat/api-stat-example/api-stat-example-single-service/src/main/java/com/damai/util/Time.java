package com.damai.util;

import java.util.Random;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-08
 **/
public class Time {
    
    public static void simulationTime() {
        Random random = new Random();
        int sleepTime = random.nextInt(2000);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
