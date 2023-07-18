package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.JobCallBackDto;
import com.example.service.JobRunRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-28
 **/
@RestController
@RequestMapping("/jobRunRecord")
@Api(tags = "jobRunRecord", description = "任务执行记录")
public class JobRunRecordController {
    
    @Autowired
    private JobRunRecordService jobRunRecordService;
    
    @RequestMapping(value = "/callBack",method = RequestMethod.POST)
    public ApiResponse<Integer> callBack(@Valid @RequestBody JobCallBackDto JobCallBackDto) {
        return ApiResponse.ok(jobRunRecordService.callBack(JobCallBackDto));
    }
    
    
}
