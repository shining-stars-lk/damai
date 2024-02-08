package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.SeatAddDto;
import com.example.service.SeatService;
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
 * 节目座位表 前端控制器
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/seat")
@Api(tags = "seat", description = "座位")
public class SeatController {
    
    @Autowired
    private SeatService seatService;
    
    
    @ApiOperation(value = "添加")
    @PostMapping(value = "/add")
    public ApiResponse<Long> add(@Valid @RequestBody SeatAddDto seatAddDto) {
        return ApiResponse.ok(seatService.add(seatAddDto));
    }
}
