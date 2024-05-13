package com.damai.context.impl;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.server.ServerWebExchange;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: Gateway的ServerWebExchange数据存放
 * @author: 阿星不是程序员
 **/
@Setter
@Getter
public class GatewayContextHolder {
    
    private static final ThreadLocal<GatewayContextHolder> THREAD_LOCAL = ThreadLocal.withInitial(GatewayContextHolder::new);

    private ServerWebExchange exchange;

    public static GatewayContextHolder getCurrentGatewayContext() {
        return THREAD_LOCAL.get();
    }

    public static void removeCurrentGatewayContext() {
        THREAD_LOCAL.remove();
    }
    
}