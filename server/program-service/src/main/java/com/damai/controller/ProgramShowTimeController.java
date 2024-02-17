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

import javax.validation.Valid;

/**
 * <p>
 * 节目演出表 前端控制器
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/program/show/time")
@Api(tags = "program-show-time", description = "节目演出时间")
public class ProgramShowTimeController {
    
    @Autowired
    private ProgramShowTimeService programShowTimeService;
    
    @ApiOperation(value = "添加")
    @PostMapping(value = "/add")
    public ApiResponse<Long> add(@Valid @RequestBody ProgramShowTimeAddDto programShowTimeAddDto) {
        return ApiResponse.ok(programShowTimeService.add(programShowTimeAddDto));
    }
}
