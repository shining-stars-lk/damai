package com.example.controller;

import com.example.distributedlock.annotion.DistributedLock;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-02-23
 **/
@RestController
public class TestController {
    
    @PostMapping(value = "/test")
    @DistributedLock(name = "test",keys = {"#id"})
    public void test(String id){
        System.out.println(id);
    }
}
