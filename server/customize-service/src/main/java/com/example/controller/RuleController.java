package com.example.controller;

import com.example.common.Result;
import com.example.dto.RuleDto;
import com.example.dto.RuleGetDto;
import com.example.dto.RuleStatusDto;
import com.example.dto.RuleUpdateDto;
import com.example.service.RuleService;
import com.example.vo.RuleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-30
 **/
@RestController
@RequestMapping("/rule")
@Api(tags = "rule", description = "规则")
public class RuleController {

    @Autowired
    private RuleService ruleService;
    
    @ApiOperation(value = "添加普通规则")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@Valid @RequestBody RuleDto ruleDto) {
        ruleService.ruleAdd(ruleDto);
        return Result.success();
    }
    
    @ApiOperation(value = "修改普通规则")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@Valid @RequestBody RuleUpdateDto ruleUpdateDto) {
        ruleService.ruleUpdate(ruleUpdateDto);
        return Result.success();
    }
    
    @ApiOperation(value = "修改普通规则状态")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public Result updateStatus(RuleStatusDto ruleStatusDto){
        ruleService.ruleUpdateStatus(ruleStatusDto);
        return Result.success();
    }
    
    @ApiOperation(value = "查询普通规则")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public Result<RuleVo> get(RuleGetDto ruleGetDto){
        return Result.success(ruleService.get(ruleGetDto));
    }
}
