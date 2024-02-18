package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.OrderCreateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 订单服务 feign
 * @author: 阿宽不是程序员
 **/
@Component
@FeignClient(value = "order-service",fallback = OrderClientFallback.class)
public interface OrderClient {
    
    /**
     * 创建订单
     * */
    @PostMapping("/order/create")
    ApiResponse<String> create(OrderCreateDto orderCreateDto);
}
