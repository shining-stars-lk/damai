package com.example.base;

import com.example.threadlocal.BaseParameterHolder;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @description: 对线程池进行MDC获取requestId增强
 * @author: k
 * @create: 2021-12-06
 **/
public class BaseThreadPoolMDC {

    /**
     * 在执行线程池任务前，先获取父线程的MDC上下文
     */

    protected static Map<String, String> getContextForTask() {
        return MDC.getCopyOfContextMap();
    }
    
    /**
     * 在执行线程池任务前，先获取父线程的hold上下文
     */
    protected static Map<String,String> getContextForHold() {
        return BaseParameterHolder.getParameterMap();
    }

    /**
     * 对要执行的execute任务进行包装
     *
     * @param runnable 任务
     * @param parentMdcContext 父线程的MDC上下文
     * @param parentHoldContext 父线程的hold上下文
     */
    protected static Runnable wrapExecute(final Runnable runnable, final Map<String, String> parentMdcContext, final Map<String, String> parentHoldContext) {
        return () -> {
            Map<String, Map<String, String>> preprocess = preprocess(parentMdcContext, parentHoldContext);
            Map<String, String> holdContext = preprocess.get("holdContext");
            Map<String, String> mdcContext = preprocess.get("mdcContext");
            try {
                //执行任务
                runnable.run();
            } finally {
                postProcess(mdcContext,holdContext);
            }
        };
    }

    /**
     * 对要执行的submit任务进行包装
     *
     * @param task    任务
     * @param parentMdcContext 父线程的MDC上下文
     * @param parentHoldContext 父线程的hold上下文
     */
    protected static <T> Callable<T> wrapSubmit(Callable<T> task, final Map<String, String> parentMdcContext, final Map<String, String> parentHoldContext) {
        return () -> {
            Map<String, Map<String, String>> preprocess = preprocess(parentMdcContext, parentHoldContext);
            Map<String, String> holdContext = preprocess.get("holdContext");
            Map<String, String> mdcContext = preprocess.get("mdcContext");
            try {
                //执行任务
                return task.call();
            } finally {
                postProcess(mdcContext,holdContext);
            }
        };
    }
    
    private static Map<String,Map<String,String>> preprocess(final Map<String, String> parentMdcContext, final Map<String, String> parentHoldContext){
        Map<String,Map<String,String>> map = new HashMap<>();
        //获取本线程的hold上下文
        Map<String, String> holdContext = BaseParameterHolder.getParameterMap();
        //获取本线程的MDC上下文
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        //如果父线程的MDC上下文为空，则清空子线程的
        if (parentMdcContext == null) {
            MDC.clear();
        } else {
            //否则将父线程的设置到这次本线程中
            MDC.setContextMap(parentMdcContext);
        }
        //如果父线程的hold上下文为空，则清空子线程的
        if (parentHoldContext == null) {
            BaseParameterHolder.removeParameterMap();
        } else {
            //否则将父线程的设置到这次本线程中
            BaseParameterHolder.setParameterMap(parentHoldContext);
        }
        map.put("holdContext",holdContext);
        map.put("mdcContext",mdcContext);
        return map;
    }
    
    private static void postProcess(Map<String, String> mdcContext, Map<String, String> holdContext){
        //如果本线程MDC上下文为空，直接清除掉
        if (mdcContext == null) {
            MDC.clear();
        } else {
            //否则，将本线程的上下文恢复回去
            MDC.setContextMap(mdcContext);
        }
        //如果本线程hold上下文为空，直接清除掉
        if (holdContext == null) {
            BaseParameterHolder.removeParameterMap();
        } else {
            //否则，将本线程的上下文恢复回去
            BaseParameterHolder.setParameterMap(holdContext);
        }
    }
}
