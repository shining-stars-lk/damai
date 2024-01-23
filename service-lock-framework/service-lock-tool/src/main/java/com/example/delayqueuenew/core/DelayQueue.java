package com.example.delayqueuenew.core;

import com.example.delayqueuenew.context.DelayQueuePart;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-23
 **/
@Slf4j
public class DelayQueue {
    private final AtomicBoolean runFlag = new AtomicBoolean(false);
    private final RBlockingQueue<String> blockingQueue;
    private final RDelayedQueue<String> delayedQueue;
    /** 拉队列线程池 */
    private final ExecutorService executorService;
    private final AtomicInteger threadCount = new AtomicInteger(1);
    
    private volatile Thread thread = null;
    
    
    public DelayQueue(DelayQueuePart delayQueuePart,String relTopic){
        this.executorService = new ThreadPoolExecutor(
                delayQueuePart.getRedissonProperties().getCorePoolSize(),
                delayQueuePart.getRedissonProperties().getMaximumPoolSize(),
                delayQueuePart.getRedissonProperties().getKeepAliveTime(),
                delayQueuePart.getRedissonProperties().getUnit(),
                new LinkedBlockingQueue<>(delayQueuePart.getRedissonProperties().getWorkQueueSize()),
                r -> new Thread(Thread.currentThread().getThreadGroup(), r, 
                        "delay-queue-consume-thread-" + threadCount.getAndIncrement()));
        
        RedissonClient redissonClient = delayQueuePart.getRedissonClient();
        this.blockingQueue = redissonClient.getBlockingQueue(relTopic);
        this.delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        
        if (Objects.isNull(this.thread) || !this.runFlag.get()) {
            synchronized (this) {
                if (Objects.isNull(thread)) {
                    this.thread = new Thread(Thread.currentThread().getThreadGroup(), () -> {
                        while (this.runFlag.get() && !Thread.interrupted()) {
                            try {
                                assert blockingQueue != null;
                                String content = blockingQueue.take();
                                executorService.execute(() -> {
                                    try {
                                        delayQueuePart.getConsumerTask().execute(content);
                                    }catch (Exception e) {
                                        log.error("consumer execute error",e);
                                    }
                                });
                            } catch (InterruptedException e) {
                                destroy();
                            } catch (Throwable e) {
                                log.error("blockingQueue take error",e);
                            }
                        }
                    }, "listen-thread");
                }
                if (!this.runFlag.get()) {
                    this.runFlag.set(true);
                    this.thread.start();
                }
            }
        }
        
    }
    
    public void offer(String content, long delayTime, TimeUnit timeUnit) {
        delayedQueue.offer(content,delayTime,timeUnit);
    }
    
    public void destroy() {
        runFlag.set(false);
        try {
            if (executorService != null) {
                executorService.shutdown();
            }
            if (delayedQueue != null) {
                delayedQueue.destroy();
            }
        } catch (Exception e) {
            log.error("destroy error",e);
        }
    }
}
