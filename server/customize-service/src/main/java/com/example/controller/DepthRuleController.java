package com.example.controller;

import com.example.common.Result;
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
 * @author: k
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
    public Result add(@Valid @RequestBody DepthRuleDto depthRuleDto) {
        depthRuleService.depthRuleAdd(depthRuleDto);
        return Result.success();
    }
    
    @ApiOperation(value = "修改深度规则")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@Valid @RequestBody DepthRuleUpdateDto depthRuleUpdateDto) {
        depthRuleService.depthRuleUpdate(depthRuleUpdateDto);
        return Result.success();
    }
    
    @ApiOperation(value = "修改深度规则装填")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public Result updateStatus(DepthRuleStatusDto depthRuleStatusDto){
        depthRuleService.depthRuleUpdateStatus(depthRuleStatusDto);
        return Result.success();
    }
    
    @ApiOperation(value = "查询深度规则")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public Result<List<DepthRuleVo>> get(){
        return Result.success(depthRuleService.selectList());
    }
}
