package com.example.core;

/**
 * @program: cook-frame
 * @description:
 * @author: lk
 * @create: 2024-01-23
 **/
public interface ConsumerTask {
    
    void execute(String content);
    
    String topic();
}
