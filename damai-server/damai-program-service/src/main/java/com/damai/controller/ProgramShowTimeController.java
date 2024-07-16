package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.ProgramShowTimeAddDto;
import com.damai.service.ProgramShowTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目演出时间 控制层
 * @author: 阿星不是程序员
 **/
@RestController
@RequestMapping("/program/show/time")
@Api(tags = "program-show-time", value = "节目演出时间")
public class ProgramShowTimeController {
    
    @Autowired
    private ProgramShowTimeService programShowTimeService;
    
    @ApiOperation(value = "添加")
    @PostMapping(value = "/add")
    public ApiResponse<Long> add(@Valid @RequestBody ProgramShowTimeAddDto programShowTimeAddDto) {
        return ApiResponse.ok(programShowTimeService.add(programShowTimeAddDto));
    }
}
