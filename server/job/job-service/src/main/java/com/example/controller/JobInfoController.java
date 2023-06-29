package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.dto.JobInfoDto;
import com.example.dto.JobInfoDtoPage;
import com.example.dto.RunJobDto;
import com.example.entity.JobInfo;
import com.example.service.JobInfoService;
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
 * @create: 2023-06-28
 **/
@RestController
@RequestMapping("/jobInfo")
public class JobInfoController {
    
    @Autowired
    private JobInfoService jobInfoService;
    
    @RequestMapping(value = "/pageList",method = RequestMethod.POST)
    public Result<Page<JobInfo>> pageList(@Valid @RequestBody JobInfoDtoPage jobInfoDtoPage) {
        Page<JobInfo> jobInfoPage = jobInfoService.pageList(jobInfoDtoPage);
        return Result.success(jobInfoPage);
    }
    
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result<Boolean> add(@Valid @RequestBody JobInfoDto jobInfoDto) {
        jobInfoService.add(jobInfoDto);
        return Result.success(true);
    }
    
    @RequestMapping(value = "/runJob",method = RequestMethod.POST)
    public Result<Object> runJob(@Valid @RequestBody RunJobDto runJobDto) {
        return Result.success(jobInfoService.runJob(runJobDto));
    }
}
