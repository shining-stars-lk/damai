package com.damai.controller;

import com.alibaba.fastjson.JSON;
import com.damai.client.BaseDataClient;
import com.damai.common.ApiResponse;
import com.damai.dto.AreaGetDto;
import com.damai.vo.AreaVo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-07-19
 **/
@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private BaseDataClient baseDataClient;
    
    @PostMapping(value = "/test")
    public ApiResponse<Void> test(@Valid @RequestBody TestDto testDto){
        AreaGetDto areaGetDto = new AreaGetDto();
        areaGetDto.setId(testDto.getId());
        ApiResponse<AreaVo> apiResponse = baseDataClient.getById(areaGetDto);
        JSON.toJSONString(apiResponse);
        return ApiResponse.ok();
    }
}
