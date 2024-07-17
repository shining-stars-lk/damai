package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.DepthRuleDto;
import com.damai.dto.DepthRuleStatusDto;
import com.damai.dto.DepthRuleUpdateDto;
import com.damai.service.DepthRuleService;
import com.damai.vo.DepthRuleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 深度规则 控制层
 * @author: 阿星不是程序员
 **/
@RestController
@RequestMapping("/depthRule")
@Tag(name = "depthRule", description = "深度规则")
public class DepthRuleController {

    @Autowired
    private DepthRuleService depthRuleService;
    
    @Operation(summary  = "添加深度规则")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResponse add(@Valid @RequestBody DepthRuleDto depthRuleDto) {
        depthRuleService.depthRuleAdd(depthRuleDto);
        return ApiResponse.ok();
    }
    
    @Operation(summary  = "修改深度规则")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ApiResponse update(@Valid @RequestBody DepthRuleUpdateDto depthRuleUpdateDto) {
        depthRuleService.depthRuleUpdate(depthRuleUpdateDto);
        return ApiResponse.ok();
    }
    
    @Operation(summary  = "修改深度规则状态")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public ApiResponse updateStatus(@Valid @RequestBody DepthRuleStatusDto depthRuleStatusDto){
        depthRuleService.depthRuleUpdateStatus(depthRuleStatusDto);
        return ApiResponse.ok();
    }
    
    @Operation(summary  = "查询深度规则")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ApiResponse<List<DepthRuleVo>> get(){
        return ApiResponse.ok(depthRuleService.selectList());
    }
}
