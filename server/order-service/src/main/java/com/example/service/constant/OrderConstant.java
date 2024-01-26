package com.example.service.constant;

import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-24
 **/
public class OrderConstant {
    
    public static final String DELAY_ORDER_CANCEL_TOPIC ="d_delay_order_cancel_topic";
    
    public static final String DELAY_ORDER_PAY_CHECK_TOPIC = "d_delay_order_pay_check_topic";
    
    public static final Long DELAY_ORDER_PAY_CHECK_TIME = 3L;
    
    public static final TimeUnit DELAY_ORDER_PAY_CHECK_TIME_UNIT = TimeUnit.SECONDS;
}
