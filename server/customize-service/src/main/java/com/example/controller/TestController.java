package com.example.controller;

import com.example.common.Result;
import com.example.dto.TestDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-30
 **/
@RestController
@RequestMapping("/test")
public class TestController {
    
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Result<String> test(@Valid @RequestBody TestDto testDto) {
        return Result.success(testDto.getId());
    }
}
