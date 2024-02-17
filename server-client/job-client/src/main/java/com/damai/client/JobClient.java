package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.JobCallBackDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Component
@FeignClient(value = "job-service",fallback = JobClientFallback.class)
public interface JobClient {
    
    @RequestMapping(value = "jobRunRecord/callBack", method = RequestMethod.POST)
    ApiResponse<Boolean> callBack(@Valid @RequestBody JobCallBackDto dto);
}
