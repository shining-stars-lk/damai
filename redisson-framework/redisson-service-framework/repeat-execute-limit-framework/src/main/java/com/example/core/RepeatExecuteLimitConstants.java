package com.example.core;

/**
 * @program: redis-example
 * @description: 分布式锁业务名管理
 * @author: 星哥
 * @create: 2023-05-28
 **/
public class RepeatExecuteLimitConstants {

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
    public final static String REGISTER_USER_LOCK = "d_register_user_lock";
    
    /**
     * 登录用户
     * */
    public final static String LOGIN_USER_LOCK = "d_login_user_lock";
    
    /**
     * 节目
     * */
    public final static String PROGRAM_LOCK = "d_program_lock";
    
    /**
     * 节目类型
     * */
    public final static String PROGRAM_CATEGORY_LOCK = "d_program_category_lock";
    
    /**
     * 取消订单
     * */
    public final static String ORDER_CANCEL_LOCK = "d_order_cancel_lock";
    
    
    /**
     * 交易状态检查
     * */
    public final static String TRADE_CHECK = "d_trade_check_lock";
    
    /**
     * 节目服务订单创建V1
     * */
    public final static String PROGRAM_ORDER_CREATE_V1 = "d_program_order_create_v1_lock";
    
    /**
     * 节目服务订单创建V2
     * */
    public final static String PROGRAM_ORDER_CREATE_V2 = "d_program_order_create_v2_lock";
    
    /**
     * 支付服务的通用支付
     * */
    public final static String COMMON_PAY = "d_common_pay_lock";
    
    /**
     * 订单服务的订单支付后状态检查
     * */
    public final static String ORDER_PAY_CHECK = "d_order_pay_check_lock";
}
