package com.example.delayqueuenew.core;

import java.util.concurrent.atomic.AtomicInteger;

/***
 * 
 * title:隔离区选择器
 *
 */
public class IsolationRegionSelector {

	private final AtomicInteger count = new AtomicInteger(0);

	private final Integer thresholdValue;

	public IsolationRegionSelector(Integer thresholdValue) {
		this.thresholdValue = thresholdValue;
	}

	private int reset() {
		count.set(0);
		return 0;
	}
	
	public synchronized int getIndex() {
		// get volidate
		int cur = count.get();
		if (cur >= thresholdValue) {
			cur = reset();
		} else {
			// incr
			count.incrementAndGet();
		}
		return cur;
	}
}
