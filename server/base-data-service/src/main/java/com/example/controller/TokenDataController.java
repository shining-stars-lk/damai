package com.example.controller;

import com.example.common.Result;
import com.example.dto.TokenDataDto;
import com.example.service.TokenDataService;
import com.example.vo.TokenDataVo;
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
 * @create: 2023-06-17
 **/
@RestController
@RequestMapping("/token/data")
public class TokenDataController {
    
    @Autowired
    private TokenDataService tokenDataService;
    
    @PostMapping (value = "/get")
    public Result<TokenDataVo> get() {
        TokenDataVo tokenDataVo = tokenDataService.get();
        return Result.success(tokenDataVo);
    }
    
    @PostMapping(value = "/add")
    public Result<Boolean> add(@Valid @RequestBody TokenDataDto tokenDataDto) {
        tokenDataService.add(tokenDataDto);
        return Result.success(true);
    }
}
