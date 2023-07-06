package com.example.controller;

import com.example.common.Result;
import com.example.dto.UserDto;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping(value = "/login")
    public Result<String> login(@Valid @RequestBody UserDto userDto) {
        return Result.success(userService.login(userDto));
    }
}
