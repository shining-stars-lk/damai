package com.example.threadlocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-05-09
 **/
public class BaseParameterHolder {
    
    private static ThreadLocal<Map<String, String>> threadLocalMap = new ThreadLocal<>();
    
    
    public static void setParameter(String name, String value) {
        Map<String, String> map = threadLocalMap.get();
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(name, value);
        threadLocalMap.set(map);
    }
    
    public static String getParameter(String name) {
        return Optional.ofNullable(threadLocalMap.get()).map(map -> map.get(name)).orElse(null);
    }
    
    public static void removeParameter(String name) {
        Map<String, String> map = threadLocalMap.get();
        if (map != null) {
            map.remove(name);
        }
    }
    
    public static ThreadLocal<Map<String, String>> getThreadLocal() {
        return threadLocalMap;
    }
    
    public static Map<String, String> getParameterMap() {
        Map<String, String> map = threadLocalMap.get();
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }
    
    public static void setParameterMap(Map<String, String> map) {
        threadLocalMap.set(map);
    }
    
    public static void removeParameterMap(){
        threadLocalMap.remove();
    }
}
