package com.luo.delayqueue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonFastDelayQueueClient {

	/***
	 * 业务标识， 存到reddison队列中的key 前缀
	 * 
	 * @return
	 */
	String businessKey() default "";

	/***
	 * title： 拉取队列数据的线程数(业务执行过慢可以稍微调大)
	 * 
	 * @return
	 */
	public int pollEventNumber() default 0;

	/***
	 * title: 延时队列的 分区数 (延时有瓶颈时 可适当调大次数，但是会耗redis cpu 性能)
	 * 
	 * @return
	 */
	public int partition() default 0;
}
