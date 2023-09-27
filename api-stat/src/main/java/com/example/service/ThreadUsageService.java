package com.example.service;


import com.example.model.ThreadInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ThreadUsageService {
    private static Logger log = Logger.getLogger(ThreadUsageService.class.toString());

    public static ThreadUsageService newInstance() {
        return new ThreadUsageService();
    }

    public List<ThreadInfo> getThreads() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        int activeCount = threadGroup.activeCount();
        Thread[] threads = new Thread[activeCount];
        threadGroup.enumerate(threads);
        List<ThreadInfo> list = new ArrayList<>();
        for (int i = 0; i < activeCount; i++) {
            Thread thread = threads[i];
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(thread.getId());
            threadInfo.setName(thread.getName());
            threadInfo.setClassType(thread.getClass().getSimpleName());
            threadInfo.setState(thread.getState().name());
            threadInfo.setInterrupted(thread.isInterrupted());
            threadInfo.setDaemon(thread.isDaemon());
            threadInfo.setPriority(thread.getPriority());
            StackTraceElement[] stackTrace = thread.getStackTrace();
            threadInfo.setStacks(Arrays.stream(stackTrace).collect(Collectors.toList()));
            list.add(threadInfo);
        }
        return list;
    }

    public List<ThreadInfo> getThreads(String state) {
        List<ThreadInfo> threads = getThreads();
        return threads.stream().filter(a -> a.getState().equals(state)).collect(Collectors.toList());
    }
}
