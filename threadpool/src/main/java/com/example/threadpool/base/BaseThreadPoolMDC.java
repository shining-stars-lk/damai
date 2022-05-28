package com.example.threadpool.base;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @program: msa-toolkit
 * @description: 对线程池进行MDC获取requestId增强
 * @author: lk
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
     * 对要执行的execute任务进行包装
     *
     * @param runnable 任务
     * @param context  父线程的MDC上下文
     */
    protected static Runnable wrapExecute(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            //获取本线程的MDC上下文
            Map<String, String> previous = MDC.getCopyOfContextMap();
            //如果父线程的MDC上下文为空，则清空子线程的
            if (context == null) {
                MDC.clear();
            } else {
                //否则将父线程的设置到这次本线程中
                MDC.setContextMap(context);
            }
            try {
                //执行任务
                runnable.run();
            } finally {
                //如果本线程MDC上下文为空，直接清除掉
                if (previous == null) {
                    MDC.clear();
                } else {
                    //否则，将本线程的上下文恢复回去
                    MDC.setContextMap(previous);
                }
            }
        };
    }

    /**
     * 对要执行的submit任务进行包装
     *
     * @param task    任务
     * @param context 父线程的MDC上下文
     */
    protected static <T> Callable<T> wrapSubmit(Callable<T> task, final Map<String, String> context) {
        return () -> {
            //获取本线程的MDC上下文
            Map<String, String> previous = MDC.getCopyOfContextMap();
            //如果父线程的MDC上下文为空，则清空子线程的
            if (context == null) {
                MDC.clear();
            } else {
                //否则将父线程的设置到这次本线程中
                MDC.setContextMap(context);
            }
            try {
                //执行任务
                return task.call();
            } finally {
                //如果本线程MDC上下文为空，直接清除掉
                if (previous == null) {
                    MDC.clear();
                } else {
                    //否则，将本线程的上下文恢复回去
                    MDC.setContextMap(previous);
                }
            }
        };
    }
}
