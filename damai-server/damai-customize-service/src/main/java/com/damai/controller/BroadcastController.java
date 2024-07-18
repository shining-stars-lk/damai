package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.BroadcastCallDto;
import com.damai.service.BroadcastService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 广播调用 控制层
 * @author: 阿星不是程序员
 **/
@RestController
@RequestMapping("/broadcast")
@Tag(name = "broadcast", description = "广播调用")
public class BroadcastController {

    @Autowired
    private BroadcastService broadcastService;
    
    @Operation(summary  = "广播调用")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResponse<Void> call(@Valid @RequestBody BroadcastCallDto broadcastCallDto) {
        broadcastService.call(broadcastCallDto);
        return ApiResponse.ok();
    }
}
