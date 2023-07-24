package com.example.agent.plugin.thread.wrapper;

import com.example.ParameterHolder;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/*
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-21
 **/
public class BaseWrapper {
    
    protected static Map<String, Map<String,String>> preprocess(final Map<String, String> parentMdcContext, final Map<String, String> parentHoldContext){
        Map<String,Map<String,String>> map = new HashMap<>();
        Map<String, String> holdContext = ParameterHolder.getParameterMap();
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        if (parentMdcContext == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(parentMdcContext);
        }
        if (parentHoldContext == null) {
            ParameterHolder.removeParameterMap();
        } else {
            ParameterHolder.setParameterMap(parentHoldContext);
        }
        map.put("holdContext",holdContext);
        map.put("mdcContext",mdcContext);
        return map;
    }
    
    protected static void postProcess(Map<String, String> mdcContext, Map<String, String> holdContext){
        if (mdcContext == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(mdcContext);
        }
        if (holdContext == null) {
            ParameterHolder.removeParameterMap();
        } else {
            ParameterHolder.setParameterMap(holdContext);
        }
    }
}
