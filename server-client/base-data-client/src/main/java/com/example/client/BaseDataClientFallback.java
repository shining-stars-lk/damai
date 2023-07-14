package com.example.client;

import com.example.common.Result;
import com.example.dto.GetChannelDataByCodeDto;
import com.example.enums.BaseCode;
import com.example.vo.GetChannelDataVo;
import com.example.vo.TokenDataVo;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-04-17
 **/
@Component
public class BaseDataClientFallback implements BaseDataClient {
    
    @Override
    public Result<GetChannelDataVo> getByCode(GetChannelDataByCodeDto dto) {
        return Result.error(BaseCode.SYSTEM_ERROR);
    }
    
    @Override
    public Result<TokenDataVo> get() {
        return Result.error(BaseCode.SYSTEM_ERROR);
    }
}
