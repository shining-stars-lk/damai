package com.damai.balance;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 负载均衡服务过滤接口
 * @author: 阿星不是程序员
 **/
public interface FilterLoadBalance {
    
    /**
     * 服务过滤操作
     * @param servers 服务列表
     * */
    void selectServer(List<ServiceInstance> servers);
}
