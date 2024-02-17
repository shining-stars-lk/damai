package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.InfoDto;
import com.damai.enums.BaseCode;
import com.damai.vo.InfoVo;
import org.springframework.stereotype.Component;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Component
public class ProviderClientFallback implements ProviderClient {
    
    @Override
    public ApiResponse<InfoVo> getInfo(final InfoDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
