package com.damai.constant;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 业务类型
 * @author: 阿星不是程序员
 **/
public class LockInfoType {
    
    /***
     * 防重复执行幂等
     */
    public static final String REPEAT_EXECUTE_LIMIT = "repeat_execute_limit";
    
    /***
     * 分布式锁
     */
    public static final String SERVICE_LOCK = "service_lock";
    
}
