package com.luo.delayqueue.context;

import com.alibaba.fastjson.JSON;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RedissonDelayQueue implements DelayQueue, Runnable {

	private volatile boolean running;
	private RBlockingQueue<Task> blockingQueue;
	private RDelayedQueue<Task> delayedQueue;
	private RedissonClientProperties properties;
	/** 拉队列线程池 */
	private ExecutorService pollExcutor;
	private static final String ThreadNameAlias = "RedissonFastDelayQueueThread-";
	private final AtomicInteger incr = new AtomicInteger(1);

	private Thread loop = null;

	private Invoker invoker = null;

	private static class Invoker {

		private Object instance;

		private Method method;

		public Invoker(RedissonClientProperties properties) {
			this.instance = properties.getBeanFactory().getBean(properties.getClientClass());
			this.method = properties.getInvokeTrigger();
		}

		public void invoke(Object object)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			method.invoke(instance, new Object[] { object });
		}

	}

	public RedissonDelayQueue(RedissonClientProperties properties) {
		this.properties = properties;
		this.invoker = new Invoker(properties);
		newPollThreadExcutor();
		newDelayQueue();
		// 注册监听器
		newLoopThread();
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

	private void newDelayQueue() {
		final RedissonClient redissonClient = properties.getRedissonClient();
		this.blockingQueue = redissonClient.getBlockingQueue(properties.getRedisKey());
		this.delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
	}

	private void newPollThreadExcutor() {
		final int poll = properties.getInt(RedissonClientProperties.PollEventNumberKey, 1);
		pollExcutor = new ThreadPoolExecutor(poll, poll, 3000, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(1024), new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						return new Thread(Thread.currentThread().getThreadGroup(), r,
								ThreadNameAlias + incr.getAndIncrement());
					}
				});
	}

	@Override
	public <T> void put(Task task) {
		delayedQueue.offer(task, task.getDelay(), task.getTimeUnit());
	}

	@Override
	public void destory() {
		running = false;
		try {
			if (pollExcutor != null) {
				pollExcutor.shutdown();
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
				final Task task = blockingQueue.take();
				pollExcutor.execute(new Runnable() {
					@Override
					public void run() {
						// 寻找 客户端实例
						try {
							invoker.invoke(JSON.parseObject(task.getData(), Class.forName(task.getDataClass())));
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
								| ClassNotFoundException e) {
							e.printStackTrace();
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				});
			} catch (InterruptedException e) {
				destory();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

}
