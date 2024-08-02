package com.damai.controller;

import com.alibaba.fastjson.JSON;
import com.damai.BusinessThreadPool;
import com.damai.client.BaseDataClient;
import com.damai.common.ApiResponse;
import com.damai.dto.AreaGetDto;
import com.damai.vo.AreaVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-07-19
 **/
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private BaseDataClient baseDataClient;
    
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    
    @PostMapping(value = "/test")
    public ApiResponse<Void> test(@Valid @RequestBody TestDto testDto){
        log.info("用户服务调用 testDto:{}",JSON.toJSONString(testDto));
        executorService.execute(() -> log.info("用户服务调用异步打印 testDto:{}",JSON.toJSONString(testDto)));
        BusinessThreadPool.execute(() -> log.info("用户服务调用自定义线程池异步打印 testDto:{}",JSON.toJSONString(testDto)));
        AreaGetDto areaGetDto = new AreaGetDto();
        areaGetDto.setId(testDto.getId());
        ApiResponse<AreaVo> apiResponse = baseDataClient.getById(areaGetDto);
        JSON.toJSONString(apiResponse);
        return ApiResponse.ok();
    }
}
