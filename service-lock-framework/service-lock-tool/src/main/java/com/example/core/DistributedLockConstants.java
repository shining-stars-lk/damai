package com.example.core;

/**
 * @program: redis-example
 * @description: 分布式锁业务名管理
 * @author: 星哥
 * @create: 2023-05-28
 **/
public class DistributedLockConstants {

    /**
     * 	分布式id datacenterId
     * */
    public static final String  DATACENTER_Id = "datacenter_id";
    
    /**
     * api统计定时任务
     * */
    public final static String API_STAT_LOCK = "api_stat_lock";
    
    /**
     * 分布式锁示例
     * */
    public final static String LOCK_DATA = "lock_data"; 

}
