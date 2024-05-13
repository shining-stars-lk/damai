package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.JobCallBackDto;
import com.damai.service.JobRunRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: job记录 控制层
 * @author: 阿星不是程序员
 **/
@RestController
@RequestMapping("/jobRunRecord")
@Api(tags = "jobRunRecord", value = "任务执行记录")
public class JobRunRecordController {
    
    @Autowired
    private JobRunRecordService jobRunRecordService;
    
    @RequestMapping(value = "/callBack",method = RequestMethod.POST)
    public ApiResponse<Integer> callBack(@Valid @RequestBody JobCallBackDto jobCallBackDto) {
        return ApiResponse.ok(jobRunRecordService.callBack(jobCallBackDto));
    }
    
    
}
