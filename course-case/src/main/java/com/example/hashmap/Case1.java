package com.example.hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-09
 **/
public class Case1 {

    public void function1(){
        Map<String,Object> map = new HashMap();
        map.put("学生1","学生1");
        map.put("学生2","学生2");
        map.put("学生3","学生3");
        map.put("学生4","学生4");
        System.out.println(map.size());
    }
    
}
