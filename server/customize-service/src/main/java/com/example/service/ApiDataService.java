package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.ApiDataDto;
import com.example.entity.ApiData;
import com.example.mapper.ApiDataMapper;
import com.example.vo.ApiDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-29
 **/

@Service
public class ApiDataService extends ServiceImpl<ApiDataMapper,ApiData> {

    @Autowired
    private ApiDataMapper apiDataMapper;
    
    public Page<ApiDataVo> pageList(final ApiDataDto dto) {
        Page<ApiData> page = Page.of(dto.getPageNo(), dto.getPageSize());
        Page<ApiDataVo> apiDataVoPage = apiDataMapper.pageList(page, dto);
        return apiDataVoPage;
    }
}
