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
    
    /**
     * 注册用户
     * */
    public final static String REGISTER_USER_LOCK = "d_mai_register_user_lock";
    
    /**
     * 登录用户
     * */
    public final static String LOGIN_USER_LOCK = "d_mai_login_user_lock";
    
    /**
     * 节目
     * */
    public final static String PROGRAM_LOCK = "d_mai_program_lock";
    
    /**
     * 节目类型
     * */
    public final static String PROGRAM_CATEGORY_LOCK = "d_mai_program_category_lock";
    
    /**
     * 取消订单
     * */
    public final static String ORDER_CANCEL_LOCK = "d_order_cancel_lock";
    
    /**
     * 支付宝回调
     * */
    public final static String ALIPAY_NOTIFY = "d_alipay_notify";
    
    /**
     * 交易状态检查
     * */
    public final static String TRADE_CHECK = "d_trade_check";
    
}
