package com.example.controller;


import com.example.common.ApiResponse;
import com.example.dto.InfoDto;
import com.example.service.ProviderService;
import com.example.vo.InfoVo;
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
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/provider")
@Api(tags = "provider", description = "提供者")
public class ProviderController {
    
    @Autowired
    private ProviderService providerService;
    
    @ApiOperation(value = "查询信息")
    @PostMapping(value = "/getInfo")
    public ApiResponse<InfoVo> getInfo(@Valid @RequestBody InfoDto infoDto) {
        return ApiResponse.ok(providerService.getInfo(infoDto));
    }
}
