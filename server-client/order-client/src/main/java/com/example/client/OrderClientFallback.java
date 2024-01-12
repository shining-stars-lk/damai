package com.example.client;

import com.example.common.ApiResponse;
import com.example.dto.OrderCreateDto;
import com.example.enums.BaseCode;
import org.springframework.stereotype.Component;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-11-17
 **/
@Component
public class OrderClientFallback implements OrderClient {
    
    @Override
    public ApiResponse<String> create(final OrderCreateDto orderCreateDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
