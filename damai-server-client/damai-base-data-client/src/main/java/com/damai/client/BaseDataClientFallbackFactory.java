package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.AreaGetDto;
import com.damai.dto.AreaSelectDto;
import com.damai.dto.GetChannelDataByCodeDto;
import com.damai.enums.BaseCode;
import com.damai.vo.AreaVo;
import com.damai.vo.GetChannelDataVo;
import com.damai.vo.TokenDataVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 基础数据服务 feign 异常工厂
 * @author: 阿星不是程序员
 **/
@Slf4j
@Component
public class BaseDataClientFallbackFactory implements FallbackFactory<BaseDataClient> {
    @Override
    public BaseDataClient create(Throwable cause) {
        log.error("baseData client error",cause);
        return new BaseDataClient() {
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
        };
    }
}
