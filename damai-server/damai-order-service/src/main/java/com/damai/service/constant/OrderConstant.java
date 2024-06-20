package com.damai.service.constant;

import java.util.concurrent.TimeUnit;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单 常量
 * @author: 阿星不是程序员
 **/
public class OrderConstant {
    
    public static final String DELAY_ORDER_CANCEL_TOPIC ="d_delay_order_cancel_topic";
    
    public static final String DELAY_OPERATE_PROGRAM_DATA_TOPIC = "d_delay_operate_program_data_topic";
    
    public static final Long DELAY_OPERATE_PROGRAM_DATA_TIME = 1L;
    
    public static final TimeUnit DELAY_OPERATE_PROGRAM_DATA_TIME_UNIT = TimeUnit.SECONDS;
}
