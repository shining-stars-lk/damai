package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.DepthRuleDto;
import com.example.dto.DepthRuleStatusDto;
import com.example.dto.DepthRuleUpdateDto;
import com.example.service.DepthRuleService;
import com.example.vo.DepthRuleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-30
 **/
@RestController
@RequestMapping("/depthRule")
@Api(tags = "depthRule", description = "深度规则")
public class DepthRuleController {

    @Autowired
    private DepthRuleService depthRuleService;
    
    @ApiOperation(value = "添加深度规则")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResponse add(@Valid @RequestBody DepthRuleDto depthRuleDto) {
        depthRuleService.depthRuleAdd(depthRuleDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "修改深度规则")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ApiResponse update(@Valid @RequestBody DepthRuleUpdateDto depthRuleUpdateDto) {
        depthRuleService.depthRuleUpdate(depthRuleUpdateDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "修改深度规则状态")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public ApiResponse updateStatus(@Valid @RequestBody DepthRuleStatusDto depthRuleStatusDto){
        depthRuleService.depthRuleUpdateStatus(depthRuleStatusDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "查询深度规则")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ApiResponse<List<DepthRuleVo>> get(){
        return ApiResponse.ok(depthRuleService.selectList());
    }
}
