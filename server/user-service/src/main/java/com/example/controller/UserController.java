package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.UserDto;
import com.example.dto.logOutDto;
import com.example.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/user")
@Api(tags = "user", description = "用户")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public ApiResponse<String> login(@Valid @RequestBody UserDto userDto) {
        return ApiResponse.ok(userService.login(userDto));
    }
    
    @ApiOperation(value = "退出登录")
    @PostMapping(value = "/login")
    public ApiResponse logOut(@Valid @RequestBody logOutDto logOutDto) {
        userService.logOut(logOutDto);
        return ApiResponse.ok();
    }
}
