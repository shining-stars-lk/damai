package com.example.controller;

import com.example.common.Result;
import com.example.dto.ChannelDataAddDto;
import com.example.dto.GetChannelDataByCodeDto;
import com.example.service.ChannelDataService;
import com.example.vo.GetChannelDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/channel/data")
@Api(tags = "channel/data", description = "渠道数据")
public class ChannelDataController {
    
    @Autowired
    private ChannelDataService channelDataService;
    
    @ApiOperation(value = "通过code查询渠道数据")
    @PostMapping (value = "/getByCode")
    public Result<GetChannelDataVo> getByCode(@Valid @RequestBody GetChannelDataByCodeDto getChannelDataByCodeDto) {
        GetChannelDataVo getChannelDataVo = channelDataService.getByCode(getChannelDataByCodeDto);
        return Result.success(getChannelDataVo);
    }
    
    @ApiOperation(value = "添加渠道数据")
    @PostMapping(value = "/add")
    public Result<Boolean> add(@Valid @RequestBody ChannelDataAddDto channelDataAddDto) {
        channelDataService.add(channelDataAddDto);
        return Result.success(true);
    }
}
