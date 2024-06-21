package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.TestDto;
import com.damai.dto.TestSendDto;
import com.damai.service.TestService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目订单 控制层
 * @author: 阿星不是程序员
 **/
@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private TestService testService;
    
    @ApiOperation(value = "测试get")
    @PostMapping(value = "/get")
    public ApiResponse<Long> get(@Valid @RequestBody TestDto testDto) {
        return ApiResponse.ok(testDto.getId() );
    }
    
    @ApiOperation(value = "测试发送消息")
    @PostMapping(value = "/send")
    public ApiResponse<Boolean> send(@Valid @RequestBody TestSendDto testSendDto) {
        return ApiResponse.ok(testService.testSend(testSendDto));
    }
    
    @ApiOperation(value = "重置消息计数器")
    @PostMapping(value = "/reset")
    public ApiResponse<Boolean> reset(@Valid @RequestBody TestSendDto testSendDto) {
        return ApiResponse.ok(testService.reset(testSendDto));
    }
    
    @ApiOperation(value = "测试kafka消费")
    @PostMapping(value = "/kafka/send")
    public ApiResponse<Boolean> kafkaSend(@Valid @RequestBody TestSendDto testSendDto) {
        return ApiResponse.ok(testService.kafkaSend(testSendDto));
    }
}
