package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.JobCallBackDto;
import com.damai.enums.BaseCode;
import org.springframework.stereotype.Component;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: job服务 feign 异常
 * @author: 阿星不是程序员
 **/
@Component
public class JobClientFallback implements JobClient {
    
    @Override
    public ApiResponse<Boolean> callBack(final JobCallBackDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
