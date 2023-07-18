package com.example.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.core.StringUtil;
import com.example.dto.ApiDataDto;
import com.example.entity.ApiData;
import com.example.mapper.ApiDataMapper;
import com.example.vo.ApiDataVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: cook-frame
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
        LambdaQueryWrapper<ApiData> queryWrapper = Wrappers.lambdaQuery(ApiData.class)
                .eq(StringUtil.isNotEmpty(dto.getApiAddress()), ApiData::getApiAddress, dto.getApiAddress())
                .eq(StringUtil.isNotEmpty(dto.getApiUrl()), ApiData::getApiUrl, dto.getApiUrl())
                .ge(Objects.nonNull(dto.getStartDate()),ApiData::getCreateTime,dto.getStartDate())
                .le(Objects.nonNull(dto.getEndDate()),ApiData::getCreateTime,dto.getEndDate());
        Page<ApiData> apiDataPage = apiDataMapper.selectPage(page,queryWrapper);
        List<ApiData> apiDataList = apiDataPage.getRecords();
        Page<ApiDataVo> apiDataPageVo = new Page<>();
        BeanUtils.copyProperties(apiDataPage,apiDataPageVo);
        List<ApiDataVo> apiDataVoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(apiDataList)) {
            apiDataVoList = apiDataList.stream().map(apiData -> {
                ApiDataVo apiDataVo = new ApiDataVo();
                BeanUtils.copyProperties(apiData,apiDataVo);
                return apiDataVo;
            }).collect(Collectors.toList());
        }
        apiDataPageVo.setRecords(apiDataVoList);
        return apiDataPageVo;
    }
}
