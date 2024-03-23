package com.damai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.damai.common.ApiResponse;
import com.damai.dto.JobInfoDto;
import com.damai.dto.JobInfoPageDto;
import com.damai.dto.RunJobDto;
import com.damai.entity.JobInfo;
import com.damai.service.JobInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: job 控制层
 * @author: 阿宽不是程序员
 **/
@RestController
@RequestMapping("/jobInfo")
@Api(tags = "jobInfo", value = "任务")
public class JobInfoController {
    
    @Autowired
    private JobInfoService jobInfoService;
    
    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/pageList",method = RequestMethod.POST)
    public ApiResponse<Page<JobInfo>> pageList(@Valid @RequestBody JobInfoPageDto jobInfoPageDto) {
        Page<JobInfo> jobInfoPage = jobInfoService.pageList(jobInfoPageDto);
        return ApiResponse.ok(jobInfoPage);
    }
    
    @ApiOperation(value = "添加任务")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ApiResponse<Boolean> add(@Valid @RequestBody JobInfoDto jobInfoDto) {
        jobInfoService.add(jobInfoDto);
        return ApiResponse.ok(true);
    }
    
    @ApiOperation(value = "执行任务")
    @RequestMapping(value = "/runJob",method = RequestMethod.POST)
    public ApiResponse<Object> runJob(@Valid @RequestBody RunJobDto runJobDto) {
        return ApiResponse.ok(jobInfoService.runJob(runJobDto));
    }
}
