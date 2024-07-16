package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.AllRuleDto;
import com.damai.service.AllRuleService;
import com.damai.vo.AllDepthRuleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 所有规则 控制层
 * @author: 阿星不是程序员
 **/
@RestController
@RequestMapping("/allRule")
@Api(tags = "allRule", value = "所有规则")
public class AllRuleController {

    @Autowired
    private AllRuleService allRuleService;
    
    
    @ApiOperation(value = "添加所有规则")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResponse<Void> add(@Valid @RequestBody AllRuleDto allRuleDto) {
        allRuleService.add(allRuleDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "查询所有规则")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ApiResponse<AllDepthRuleVo> get() {
        return ApiResponse.ok(allRuleService.get());
    }
}
