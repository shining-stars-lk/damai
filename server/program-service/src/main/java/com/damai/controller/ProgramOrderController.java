package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.ProgramOrderCreateDto;
import com.damai.lock.ProgramOrderLock;
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
 * 节目订单 前端控制器
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/program/order")
@Api(tags = "program-order", description = "节目订单")
public class ProgramOrderController {
    
    @Autowired
    private ProgramOrderLock programOrderLock;
    
    @ApiOperation(value = "生成订单")
    @PostMapping(value = "/create/v1")
    public ApiResponse<String> createV1(@Valid @RequestBody ProgramOrderCreateDto programOrderCreateDto) {
        return ApiResponse.ok(programOrderLock.createV1(programOrderCreateDto));
    }
    
    @ApiOperation(value = "生成订单(优化版本v2)")
    @PostMapping(value = "/create/v2")
    public ApiResponse<String> createV2(@Valid @RequestBody ProgramOrderCreateDto programOrderCreateDto) {
        return ApiResponse.ok(programOrderLock.createV2(programOrderCreateDto));
    }
    
    @ApiOperation(value = "生成订单(优化版本v3)")
    @PostMapping(value = "/create/v3")
    public ApiResponse<String> createNew(@Valid @RequestBody ProgramOrderCreateDto programOrderCreateDto) {
        return ApiResponse.ok(programOrderLock.createV3(programOrderCreateDto));
    }
}
