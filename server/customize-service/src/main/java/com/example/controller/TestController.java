package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.TestDto;
import com.example.util.DateUtils;
import com.example.vo.TestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-30
 **/
@RestController
@RequestMapping("/test")
@Api(tags = "test", description = "测试")
public class TestController {
    
    @ApiOperation(value = "测试")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ApiResponse<Integer> test(@Valid @RequestBody TestDto testDto) {
        if (testDto.getSleepTime() != null) {
            try {
                Thread.sleep(testDto.getSleepTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return ApiResponse.ok(testDto.getId());
    }
    
    @ApiOperation(value = "测试V2")
    @RequestMapping(value = "/testV2", method = RequestMethod.POST)
    public ApiResponse<TestVo> testV2(@Valid @RequestBody TestDto testDto) {
        if (testDto.getSleepTime() != null) {
            try {
                Thread.sleep(testDto.getSleepTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        TestVo testVo = new TestVo();
        testVo.setId1(222L);
        testVo.setId2(222);
        testVo.setId3(new BigDecimal("222"));
        testVo.setTime1(DateUtils.now());
        testVo.setLocalDateTime(LocalDateTime.now());
        testVo.setLocalDate(LocalDate.now());
        testVo.setLocalTime(LocalTime.now());
        return ApiResponse.ok(testVo);
    }
}
