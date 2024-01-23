package com.luo.delayqueue.context;

public class DelayQueueComponent implements DelayQueue {
	/***
	 * 分区选择器
	 */
	private PartitionChooser partitionChooser = null;

	private DelayQueue[] delayQueues = null;

	public DelayQueueComponent(RedissonClientProperties properties) {
		final int partition = properties.getInt(RedissonClientProperties.PartitionsKey,
				RedissonClientProperties.DefaultPartitions);
		this.partitionChooser = new PollPartitionChooser(partition);
		this.delayQueues = new DelayQueue[partition];
		// 初始化每个分区
		for (int i = 0; i < partition; i++) {
			properties.setRedisKey(i);
			delayQueues[i] = new RedissonDelayQueue(properties);
		}
	}

	@Override
	public <T> void put(Task task) {
		final int partition = partitionChooser.next();
		delayQueues[partition].put(task);
		System.out.println("fast-delay-queue ѡ��partition: " + partition);

	}

	@Override
	public void destory() {
		for (DelayQueue delayQueue : delayQueues) {
			delayQueue.destory();
		}
	}

}
