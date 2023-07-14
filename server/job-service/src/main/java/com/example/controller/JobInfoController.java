package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.dto.JobInfoDto;
import com.example.dto.JobInfoDtoPage;
import com.example.dto.RunJobDto;
import com.example.entity.JobInfo;
import com.example.service.JobInfoService;
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
 * @create: 2023-06-28
 **/
@RestController
@RequestMapping("/jobInfo")
@Api(tags = "jobInfo", description = "任务")
public class JobInfoController {
    
    @Autowired
    private JobInfoService jobInfoService;
    
    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/pageList",method = RequestMethod.POST)
    public Result<Page<JobInfo>> pageList(@Valid @RequestBody JobInfoDtoPage jobInfoDtoPage) {
        Page<JobInfo> jobInfoPage = jobInfoService.pageList(jobInfoDtoPage);
        return Result.success(jobInfoPage);
    }
    
    @ApiOperation(value = "添加任务")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result<Boolean> add(@Valid @RequestBody JobInfoDto jobInfoDto) {
        jobInfoService.add(jobInfoDto);
        return Result.success(true);
    }
    
    @ApiOperation(value = "执行任务")
    @RequestMapping(value = "/runJob",method = RequestMethod.POST)
    public Result<Object> runJob(@Valid @RequestBody RunJobDto runJobDto) {
        return Result.success(jobInfoService.runJob(runJobDto));
    }
}
