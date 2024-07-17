package com.damai.controller;

import com.alibaba.fastjson.JSON;
import com.damai.common.ApiResponse;
import com.damai.dto.TestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 测试 控制层
 * @author: 阿星不是程序员
 **/
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    
    @Operation(summary  = "添加普通规则")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ApiResponse<Boolean> test(@Valid @RequestBody TestDto testDto) {
        log.info("dto : {}", JSON.toJSONString(testDto));
        return ApiResponse.ok(true);
    }
}
