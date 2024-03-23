package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.OrderCreateDto;
import com.damai.enums.BaseCode;
import org.springframework.stereotype.Component;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单服务 feign 异常
 * @author: 阿宽不是程序员
 **/
@Component
public class OrderClientFallback implements OrderClient {
    
    @Override
    public ApiResponse<String> create(final OrderCreateDto orderCreateDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
