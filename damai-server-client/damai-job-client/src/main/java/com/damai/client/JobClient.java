package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.JobCallBackDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;

import static com.damai.constant.Constant.SPRING_INJECT_PREFIX_DISTINCTION_NAME;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: job服务 feign
 * @author: 阿星不是程序员
 **/
@Component
@FeignClient(value = SPRING_INJECT_PREFIX_DISTINCTION_NAME+"-"+"job-service",fallback = JobClientFallback.class)
public interface JobClient {
    
    /**
     * 上报任务状态
     * @param dto 参数
     * @return 结果
     * */
    @RequestMapping(value = "jobRunRecord/callBack", method = RequestMethod.POST)
    ApiResponse<Boolean> callBack(@Valid @RequestBody JobCallBackDto dto);
}
