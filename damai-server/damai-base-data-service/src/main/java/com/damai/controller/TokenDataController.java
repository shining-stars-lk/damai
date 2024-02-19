package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.TokenDataDto;
import com.damai.service.TokenDataService;
import com.damai.vo.TokenDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: token 控制层
 * @author: 阿宽不是程序员
 **/
@RestController
@RequestMapping("/token/data")
@Api(tags = "token-data", value = "token配置数据")
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
