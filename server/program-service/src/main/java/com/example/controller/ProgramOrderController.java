package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.ProgramOrderCreateDto;
import com.example.service.ProgramOrderService;
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
@Api(tags = "program/order", description = "节目订单")
public class ProgramOrderController {
    
    @Autowired
    private ProgramOrderService programOrderService;
    
    @ApiOperation(value = "生成订单")
    @PostMapping(value = "/create")
    public ApiResponse<String> create(@Valid @RequestBody ProgramOrderCreateDto programOrderCreateDto) {
        return ApiResponse.ok(programOrderService.create(programOrderCreateDto));
    }
    
    @ApiOperation(value = "生成订单(优化版本)")
    @PostMapping(value = "/create/new")
    public ApiResponse<String> createNew(@Valid @RequestBody ProgramOrderCreateDto programOrderCreateDto) {
        return ApiResponse.ok(programOrderService.createNew(programOrderCreateDto));
    }
}
