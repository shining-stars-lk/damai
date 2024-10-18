package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-10-10
 **/
@RestController
@RequestMapping("/test")
public class Test1Controller {
    
    @Autowired
    private TestService testService;
    
    @PostMapping(value = "/testLock")
    public ApiResponse<Void> testLock() {
        testService.testRedissonLock();
        return ApiResponse.ok();
    }
}
