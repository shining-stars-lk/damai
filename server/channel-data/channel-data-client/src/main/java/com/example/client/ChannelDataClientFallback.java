package com.example.client;

import com.example.common.Result;
import com.example.vo.GetChannelDataVo;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Component
public class ChannelDataClientFallback implements ChannelDataClient {
    
    @Override
    public Result<GetChannelDataVo> getByCode(final String code) {
        return null;
    }
}
