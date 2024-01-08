package com.example.client;

import com.example.common.ApiResponse;
import com.example.dto.AreaGetDto;
import com.example.dto.AreaSelectDto;
import com.example.dto.GetChannelDataByCodeDto;
import com.example.enums.BaseCode;
import com.example.vo.AreaVo;
import com.example.vo.GetChannelDataVo;
import com.example.vo.TokenDataVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Component
public class BaseDataClientFallback implements BaseDataClient {
    
    @Override
    public ApiResponse<GetChannelDataVo> getByCode(GetChannelDataByCodeDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
    
    @Override
    public ApiResponse<TokenDataVo> get() {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
    
    @Override
    public ApiResponse<List<AreaVo>> selectByIdList(final AreaSelectDto areaSelectDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
    
    @Override
    public ApiResponse<AreaVo> getById(final AreaGetDto areaGetDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
