package com.tool.controller;

import com.tool.servicelock.annotion.ServiceLock;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-02-23
 **/
@RestController
public class TestLockController {
    
    @PostMapping(value = "/test")
    @ServiceLock(name = "test",keys = {"#id"})
    public void test(String id){
        System.out.println(id);
    }
}
