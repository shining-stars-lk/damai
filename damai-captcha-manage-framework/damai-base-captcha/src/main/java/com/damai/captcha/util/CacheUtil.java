/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package com.damai.captcha.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: CacheUtil
 * @author: 阿星不是程序员
 **/
public final class CacheUtil {
    private static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    private static final Map<String, Object> CACHE_MAP = new ConcurrentHashMap<String, Object>();

    /**
     * 缓存最大个数
     */
    private static Integer CACHE_MAX_NUMBER = 1000;

    private static final Integer TWO = 2;
    /**
     * 初始化
     * @param cacheMaxNumber 缓存最大个数
     * @param second 定时任务 秒执行清除过期缓存
     */
    public static void init(int cacheMaxNumber, long second) {
        CACHE_MAX_NUMBER = cacheMaxNumber;
        if (second > 0L) {
            scheduledExecutor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r,"thd-captcha-cache-clean");
				}
			},new ThreadPoolExecutor.CallerRunsPolicy());
            scheduledExecutor.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			},10,second,TimeUnit.SECONDS);

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    if(Objects.nonNull(scheduledExecutor)){
                        clear();
                        scheduledExecutor.shutdownNow();
                    }
                }
            }));
        }
    }

    private static ScheduledExecutorService scheduledExecutor;

    /**
     * 缓存刷新,清除过期数据
     */
    public static void refresh(){
        logger.debug("local缓存刷新,清除过期数据");
        for (String key : CACHE_MAP.keySet()) {
            exists(key);
        }
    }


    public static void set(String key, String value, long expiresInSeconds){
        //设置阈值，达到即clear缓存
        if (CACHE_MAP.size() > CACHE_MAX_NUMBER * TWO) {
            logger.info("CACHE_MAP达到阈值，clear map");
            clear();
        }
        CACHE_MAP.put(key, value);
        if(expiresInSeconds >0) {
            //缓存失效时间
			CACHE_MAP.put(key + "_HoldTime", System.currentTimeMillis() + expiresInSeconds * 1000);
		}
    }

    public static void delete(String key){
        CACHE_MAP.remove(key);
        CACHE_MAP.remove(key + "_HoldTime");
    }

    public static boolean exists(String key){
        Long cacheHoldTime = (Long) CACHE_MAP.get(key + "_HoldTime");
        if (cacheHoldTime == null || cacheHoldTime == 0L) {
            return false;
        }
        if (cacheHoldTime < System.currentTimeMillis()) {
            delete(key);
            return false;
        }
        return true;
    }


    public static String get(String key){
        if (exists(key)) {
            return (String)CACHE_MAP.get(key);
        }
        return null;
    }

    /**
     * 删除所有缓存
     */
    public static void clear() {
        logger.debug("have clean all key !");
        CACHE_MAP.clear();
    }
}
