package com.example.controller;

import com.example.common.Result;
import com.example.dto.AllRuleDto;
import com.example.service.AllRuleService;
import com.example.vo.AllDepthRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-07-11
 **/
@RestController
@RequestMapping("/allRule")
public class AllRuleController {

    @Autowired
    private AllRuleService allRuleService;
    
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@Valid @RequestBody AllRuleDto allRuleDto) {
        allRuleService.add(allRuleDto);
        return Result.success();
    }
    
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public Result<AllDepthRuleVo> get() {
        return Result.success(allRuleService.get());
    }
}
