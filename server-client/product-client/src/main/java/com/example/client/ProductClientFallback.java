package com.example.client;

import com.example.dto.GetDto;
import com.example.vo.GetVo;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-04-17
 **/
@Component
public class ProductClientFallback implements ProductClient{
    @Override
    public GetVo get(final GetDto dto) {
       return null;
    }
    
    @Override
    public GetVo getV2(final GetDto dto) {
        return null;
    }
}
