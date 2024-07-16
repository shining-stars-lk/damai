package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.AreaGetDto;
import com.damai.dto.AreaSelectDto;
import com.damai.dto.GetChannelDataByCodeDto;
import com.damai.enums.BaseCode;
import com.damai.vo.AreaVo;
import com.damai.vo.GetChannelDataVo;
import com.damai.vo.TokenDataVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户服务 feign 异常
 * @author: 阿星不是程序员
 **/
@Component
public class BaseDataClientFallback implements BaseDataClient{
    @Override
    public ApiResponse<GetChannelDataVo> getByCode(final GetChannelDataByCodeDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
    
    @Override
    public ApiResponse<TokenDataVo> get() {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
    
    @Override
    public ApiResponse<List<AreaVo>> selectByIdList(final AreaSelectDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
    
    @Override
    public ApiResponse<AreaVo> getById(final AreaGetDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
