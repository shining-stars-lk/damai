package com.example.controller;


import com.example.common.ApiResponse;
import com.example.dto.GetLockDataDto;
import com.example.dto.LockDataDto;
import com.example.service.LockDataService;
import com.example.vo.LockDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-27
 **/
@RestController
@RequestMapping(value = "lock/data")
@Api(tags = "lockData", description = "分布式锁示例")
public class LockDataController {
    
    @Autowired
    private LockDataService lockDataService;
    
    @ApiOperation(value = "查询数据")
    @PostMapping(value = "/get")
    public ApiResponse<LockDataVo> get(@Valid @RequestBody GetLockDataDto getLockDataDto){
        return ApiResponse.ok(lockDataService.get(getLockDataDto));
    }
    
    @ApiOperation(value = "添加数据")
    @PostMapping(value = "/add")
    public ApiResponse<Boolean> addServiceLock(@Valid @RequestBody LockDataDto lockDataDto){
        lockDataService.addServiceLock(lockDataDto);
        return ApiResponse.ok(true);
    }
    
    @ApiOperation(value = "添加库存")
    @PostMapping(value = "/add/stock")
    public ApiResponse<Boolean> addServiceLockStock(@Valid @RequestBody LockDataDto lockDataDto){
        lockDataService.addServiceLockStock(lockDataDto);
        return ApiResponse.ok(true);
    }
    

}
