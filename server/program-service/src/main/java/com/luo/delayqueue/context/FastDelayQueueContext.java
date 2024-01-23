package com.luo.delayqueue.context;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class FastDelayQueueContext {

	private static final Map<String, DelayQueue> businessKeyInstance = new ConcurrentHashMap<>();

	private static final Map<Class<?>, String> classInstance = new ConcurrentHashMap<>();

	public static void putContext(Class<?> clazz, String businessKey, DelayQueue queue) {
		businessKeyInstance.putIfAbsent(businessKey, queue);
		classInstance.putIfAbsent(clazz, businessKey);
	}

	/***
	 * title: 向延时队列里面 放入延时任务
	 * 
	 * @param receiveClientClass 延时任务触发执行的 客户端
	 * @param task
	 */
	public static void send(Class<?> receiveClientClass, Task task) {
		if (null == receiveClientClass) {
			return;
		}
		String businessKey = classInstance.get(receiveClientClass);

		if (StringUtils.isEmpty(businessKey)) {
			throw new RuntimeException(
					receiveClientClass.getName() + " 不是一个 延时队列接收客户端，请配置@RedissonDelayFastQueueClient");
		}
		businessKeyInstance.get(businessKey).put(task);
	}

	/***
	 * title: 向延时队列里面 放入延时任务
	 *
	 * @param receiveClientClass 延时任务触发执行的 客户端
	 * @param data               数据对象
	 * @param delaySecond        延时的秒数
	 */
	public static void send(Class<?> receiveClientClass, Object data, long delaySecond) {
		if (null == receiveClientClass || data == null) {
			return;
		}
		String businessKey = classInstance.get(receiveClientClass);

		if (StringUtils.isEmpty(businessKey)) {
			throw new RuntimeException(
					receiveClientClass.getName() + " 不是一个 延时队列接收客户端，请配置@RedissonDelayFastQueueClient");
		}
		businessKeyInstance.get(businessKey).put(Task.newTask(data, delaySecond));
	}
	
	/***
	 * title: 向延时队列里面 放入延时任务
	 *
	 * @param receiveClientClass 延时任务触发执行的 客户端
	 * @param data               数据对象
	 * @param delay              延时的数值
	 * @param timeUnit           延时的数值单位
	 */
	public static void send(Class<?> receiveClientClass, Object data, long delay, TimeUnit timeUnit) {
		if (null == receiveClientClass || data == null) {
			return;
		}
		String businessKey = classInstance.get(receiveClientClass);

		if (StringUtils.isEmpty(businessKey)) {
			throw new RuntimeException(
					receiveClientClass.getName() + " 不是一个 延时队列接收客户端，请配置@RedissonDelayFastQueueClient");
		}
		businessKeyInstance.get(businessKey).put(Task.newTask(data, delay, timeUnit));
	}

}
