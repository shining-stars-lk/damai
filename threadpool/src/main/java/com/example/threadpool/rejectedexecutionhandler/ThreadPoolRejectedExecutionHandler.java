package com.example.threadpool.rejectedexecutionhandler;

import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: msa-toolkit
 * @description: 线程池异常处理
 * @author: lk
 * @create: 2021-12-06
 **/
public class ThreadPoolRejectedExecutionHandler {

    /**
     * 服务应用名
     */
    private static String threadPoolApplicationName = "business";

    /**
     * 业务线程池快速拒绝
     */
    public static class BusinessAbortPolicy implements RejectedExecutionHandler {

        public BusinessAbortPolicy() {
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            throw new RejectedExecutionException("ThreadPoolApplicationName " + threadPoolApplicationName + " Task " + r.toString() +
                    " rejected from " +
                    executor.toString());
        }
    }
}
