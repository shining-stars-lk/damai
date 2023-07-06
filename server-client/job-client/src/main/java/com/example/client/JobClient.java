package com.example.client;

import com.example.common.Result;
import com.example.dto.JobCallBackDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Component
@FeignClient(value = "job-service",fallback = JobClientFallback.class)
public interface JobClient {
    
    @RequestMapping(value = "jobRunRecord/callBack", method = RequestMethod.POST)
    Result<Boolean> callBack(@Valid @RequestBody JobCallBackDto dto);
}
