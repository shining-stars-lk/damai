package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.TokenDataDto;
import com.example.service.TokenDataService;
import com.example.vo.TokenDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-17
 **/
@RestController
@RequestMapping("/token/data")
@Api(tags = "token/data", description = "token配置数据")
public class TokenDataController {
    
    @Autowired
    private TokenDataService tokenDataService;
    
    @ApiOperation(value = "获取token配置数据")
    @PostMapping (value = "/get")
    public ApiResponse<TokenDataVo> get() {
        TokenDataVo tokenDataVo = tokenDataService.get();
        return ApiResponse.ok(tokenDataVo);
    }
    
    @ApiOperation(value = "添加token配置数据")
    @PostMapping(value = "/add")
    public ApiResponse<Boolean> add(@Valid @RequestBody TokenDataDto tokenDataDto) {
        tokenDataService.add(tokenDataDto);
        return ApiResponse.ok(true);
    }
}
