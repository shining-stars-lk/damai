package com.example;

import java.util.concurrent.*;

public class ApiStatThreadPool {

    private ThreadPoolExecutor threadPoolExecutor = null;

    public ApiStatThreadPool(){
        threadPoolExecutor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() + 1,
                Runtime.getRuntime().availableProcessors() * 2,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000));
    }

    public void execute(Runnable runnable){
        threadPoolExecutor.execute(runnable);
    }

    public <T> Future<T> submit(Callable<T> task){
        return threadPoolExecutor.submit(task);
    }
}
