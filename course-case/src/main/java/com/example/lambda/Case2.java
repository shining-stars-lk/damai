package com.example.lambda;

import com.alibaba.fastjson.JSON;
import com.example.entity.User;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-09
 **/
public class Case2 {
    
    public static void main(String[] args) {
        TestCall testCall1 = new TestCall() {
            @Override
            public String call(final Object object) {
                return JSON.toJSONString(object);
            }
        };
        System.out.println("testCall.call执行结果:" + testCall1.call(new User(1,20,"张三")));
        
        
        TestCall testCall2 = object -> {
            return JSON.toJSONString(object);
        };
        System.out.println("testCall.call执行结果:" + testCall2.call(new User(1,20,"张三")));
        
        TestCall testCall3 = object -> JSON.toJSONString(object);
        System.out.println("testCall.call执行结果:" + testCall3.call(new User(1,20,"张三")));
        
        TestCall testCall4 = JSON::toJSONString;
        System.out.println("testCall.call执行结果:" + testCall4.call(new User(1,20,"张三")));
    }
}
