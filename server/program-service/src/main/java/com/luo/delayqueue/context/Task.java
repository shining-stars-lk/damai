package com.luo.delayqueue.context;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 *
 * title: 延时 任务
 *
 * @author HadLuo
 * @date 2020-9-15 10:44:40
 */
public class Task implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5743096866266963934L;
	
	/**
	 * 数据
	 */
	private String data;
	
	/**
	 * 延时时间值
	 */
	private long delay;
	
	private TimeUnit timeUnit;
	
	private String dataClass;
	
	private Task() {
	}
	
	/***
	 * title: 构建任务
	 *
	 * @param data        任务对象
	 * @param delaySecond 延时 执行的秒数
	 * @return
	 */
	public static Task newTask(Object data, long delaySecond) {
		Task task = new Task();
		task.setData(JSON.toJSONString(data));
		task.setDataClass(data.getClass().getName());
		task.setDelay(delaySecond);
		task.setTimeUnit(TimeUnit.SECONDS);
		return task;
	}
	
	/***
	 * title: 构建任务
	 *
	 * @param data     任务对象
	 * @param delay    延时 执行的值
	 * @param timeUnit 延时执行值的单位
	 * @return
	 */
	public static Task newTask(Object data, long delay, TimeUnit timeUnit) {
		Task task = new Task();
		task.setData(JSON.toJSONString(data));
		task.setDataClass(data.getClass().getName());
		task.setDelay(delay);
		task.setTimeUnit(timeUnit);
		return task;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public long getDelayMilliSecond() {
		if (timeUnit == TimeUnit.SECONDS) {
			return delay * 1000;
		}
		return delay;
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	public String getDataClass() {
		return dataClass;
	}
	
	public void setDataClass(String dataClass) {
		this.dataClass = dataClass;
	}
	
}
