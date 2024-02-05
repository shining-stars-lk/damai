package com.example.controller;


import com.example.common.ApiResponse;
import com.example.dto.GetLockDataDto;
import com.example.dto.LockDataDto;
import com.example.service.LockDataService;
import com.example.util.ServiceLockTool;
import com.example.vo.LockDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.example.core.DistributedLockConstants.LOCK_DATA;

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
    
    @Autowired
    private ServiceLockTool serviceLockTool;
    
    
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
    
    @ApiOperation(value = "添加库存(切面锁方式)")
    @PostMapping(value = "/add/stock/service/lock")
    public ApiResponse<Boolean> addStockServiceLock(@Valid @RequestBody LockDataDto lockDataDto){
        lockDataService.addStockServiceLock(lockDataDto);
        return ApiResponse.ok(true);
    }
    
    @ApiOperation(value = "添加库存(代码方式)")
    @PostMapping(value = "/add/stock/service/lock/v2")
    public ApiResponse<Boolean> addStockServiceLockV2(@Valid @RequestBody LockDataDto lockDataDto){
        String[] params = {String.valueOf(lockDataDto.getId())};
        serviceLockTool.execute(() -> lockDataService.addStock(lockDataDto),LOCK_DATA,params);
        return ApiResponse.ok(true);
    }

}
