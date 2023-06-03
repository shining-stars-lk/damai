package com.example.controller;

import com.example.common.Result;
import com.example.dto.UserDto;
import com.example.service.UserService;
import com.example.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping(value = "/login")
    public Result<UserVo> login(@RequestBody UserDto userDto) {
        userService.login(userDto);
        return Result.success();
    }
}
