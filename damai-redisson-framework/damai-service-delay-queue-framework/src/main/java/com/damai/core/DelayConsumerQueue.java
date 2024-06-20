package com.damai.core;

import com.damai.context.DelayQueuePart;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 延迟队列 消费
 * @author: 阿星不是程序员
 **/
@Slf4j
public class DelayConsumerQueue extends DelayBaseQueue{
    
    private final AtomicInteger listenStartThreadCount = new AtomicInteger(1);
    
    private final AtomicInteger executeTaskThreadCount = new AtomicInteger(1);
    
    private final ThreadPoolExecutor listenStartThreadPool;
    
    private final ThreadPoolExecutor executeTaskThreadPool;
    
    private final AtomicBoolean runFlag = new AtomicBoolean(false);
    
    private final ConsumerTask consumerTask;
    
    public DelayConsumerQueue(DelayQueuePart delayQueuePart, String relTopic){
        super(delayQueuePart.getDelayQueueBasePart().getRedissonClient(),relTopic);
        this.listenStartThreadPool = new ThreadPoolExecutor(1,1,60, 
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(),r -> new Thread(Thread.currentThread().getThreadGroup(), r,
                "listen-start-thread-" + listenStartThreadCount.getAndIncrement()));
        this.executeTaskThreadPool = new ThreadPoolExecutor(
                delayQueuePart.getDelayQueueBasePart().getDelayQueueProperties().getCorePoolSize(),
                delayQueuePart.getDelayQueueBasePart().getDelayQueueProperties().getMaximumPoolSize(),
                delayQueuePart.getDelayQueueBasePart().getDelayQueueProperties().getKeepAliveTime(),
                delayQueuePart.getDelayQueueBasePart().getDelayQueueProperties().getUnit(),
                new LinkedBlockingQueue<>(delayQueuePart.getDelayQueueBasePart().getDelayQueueProperties().getWorkQueueSize()),
                r -> new Thread(Thread.currentThread().getThreadGroup(), r, 
                        "delay-queue-consume-thread-" + executeTaskThreadCount.getAndIncrement()));
        this.consumerTask = delayQueuePart.getConsumerTask();
    }
    
    public synchronized void listenStart(){
        if (!runFlag.get()) {
            runFlag.set(true);
            listenStartThreadPool.execute(() -> {
                while (!Thread.interrupted()) {
                    try {
                        assert blockingQueue != null;
                        String content = blockingQueue.take();
                        executeTaskThreadPool.execute(() -> {
                            try {
                                consumerTask.execute(content);
                            }catch (Exception e) {
                                log.error("consumer execute error",e);
                            }
                        });
                    } catch (InterruptedException e) {
                        destroy(executeTaskThreadPool);
                    } catch (Throwable e) {
                        log.error("blockingQueue take error",e);
                    }
                }
            });
        }
    }
    
    public void destroy(ExecutorService executorService) {
        try {
            if (Objects.nonNull(executorService)) {
                executorService.shutdown();
            }
        } catch (Exception e) {
            log.error("destroy error",e);
        }
    }
}
