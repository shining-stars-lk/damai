package com.example.client;

import com.example.common.ApiResponse;
import com.example.dto.JobCallBackDto;
import com.example.enums.BaseCode;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Component
public class JobClientFallback implements JobClient {
    
    @Override
    public ApiResponse<Boolean> callBack(final JobCallBackDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
