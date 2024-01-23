package com.luo.delayqueue.context;

/**
 * 
 * title: 延时队列
 *
 * @author HadLuo
 * @date 2020-9-15 10:54:17
 */
public interface DelayQueue {
	/**
	 * 
	 * title: 添加
	 *
	 * @param      <T>
	 * @param task
	 * @author HadLuo 2020-9-15 10:48:01
	 */
	public <T> void put(Task task);

	/**
	 * 
	 * title: 閿?姣侀槦鍒?
	 *
	 * @author HadLuo 2020-9-15 10:48:10
	 */
	public void destory();

}
