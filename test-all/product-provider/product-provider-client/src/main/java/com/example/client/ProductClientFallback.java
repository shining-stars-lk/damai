package com.example.client;

import com.example.dto.GetDto;
import com.example.vo.GetVo;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
public class ProductClientFallback implements ProductClient{
    @Override
    public GetVo get(final GetDto dto) {
        return null;
    }
}
