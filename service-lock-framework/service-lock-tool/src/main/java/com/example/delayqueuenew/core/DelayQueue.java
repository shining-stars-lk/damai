package com.example.delayqueuenew.core;

import com.example.delayqueuenew.context.DelayQueuePart;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-23
 **/
public class DelayQueue implements Runnable {
    
    private final DelayQueuePart delayQueuePart;
    
    private volatile boolean running;
    private RBlockingQueue<String> blockingQueue;
    private RDelayedQueue<String> delayedQueue;
    /** 拉队列线程池 */
    private ExecutorService executorService;
    private static final String ThreadNameAlias = "RedissonFastDelayQueueThread-";
    private final AtomicInteger incr = new AtomicInteger(1);
    
    private Thread loop = null;
    
    public DelayQueue(DelayQueuePart delayQueuePart){
        this.delayQueuePart = delayQueuePart;
        //初始化线程池
        newPollThreadExecutor();
        //初始化队列
        newDelayQueue();
        //将监听队列的任务启动
        newLoopThread();
    }
    
    public void put(String content, long delayTime, TimeUnit timeUnit) {
        delayedQueue.offer(content,delayTime,timeUnit);
    }
    
    private void newPollThreadExecutor() {
        Integer threadCount = delayQueuePart.getThreadCount();
        executorService = new ThreadPoolExecutor(threadCount, threadCount, 3000, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024),
                r -> new Thread(Thread.currentThread().getThreadGroup(), r, ThreadNameAlias + incr.getAndIncrement()));
    }
    
    private void newDelayQueue() {
        final RedissonClient redissonClient = delayQueuePart.getRedissonClient();
        this.blockingQueue = redissonClient.getBlockingQueue(delayQueuePart.getRelTopic());
        this.delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
    }
    
    private synchronized void newLoopThread() {
        if (loop == null) {
            loop = new Thread(Thread.currentThread().getThreadGroup(), this, ThreadNameAlias + "MainLoop");
            if (!running) {
                running = true;
                loop.start();
            }
        }
    }
    
    public void destroy() {
        running = false;
        try {
            if (executorService != null) {
                executorService.shutdown();
            }
            if (delayedQueue != null) {
                delayedQueue.destroy();
            }
        } catch (Exception e) {
        }
    }
    
    @Override
    public void run() {
        while (running && !Thread.interrupted()) {
            try {
                String content = blockingQueue.take();
                executorService.execute(() -> {
                    try {
                        delayQueuePart.getConsumerTask().execute(content);
                    }catch (Exception e) {
                        
                    }
                });
            } catch (InterruptedException e) {
                destroy();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
