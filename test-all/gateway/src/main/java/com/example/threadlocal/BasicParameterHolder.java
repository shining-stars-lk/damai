package com.example.threadlocal;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-05-09
 **/
public class BasicParameterHolder {
    
    private static ThreadLocal<Map<String,String>> threadLocalMap = new ThreadLocal<>();
    
    
    static {
        Map<String,String> map = new HashMap<>();
        threadLocalMap.set(map);
    }
    
    public void setParameter(String name,String value) {
        Map<String, String> map = threadLocalMap.get();
        map.put(name,value);
        threadLocalMap.set(map);
    }
    
    public String getParameter(String name) {
        return threadLocalMap.get().get(name);
    }
}
