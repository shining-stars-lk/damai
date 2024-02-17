package com.damai.threadlocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 线程绑定工具
 * @author: 阿宽不是程序员
 **/
public class BaseParameterHolder {
    
    private static final ThreadLocal<Map<String, String>> threadLocalMap = new ThreadLocal<>();
    
    
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
