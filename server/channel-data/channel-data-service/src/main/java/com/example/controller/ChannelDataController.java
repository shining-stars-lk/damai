package com.example.controller;

import com.example.common.Result;
import com.example.service.ChannelDataService;
import com.example.vo.GetChannelDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/channel/data")
public class ChannelDataController {
    
    @Autowired
    private ChannelDataService channelDataService;
    
    @GetMapping (value = "/getByCode")
    public Result<GetChannelDataVo> getByCode(@RequestParam(value = "code") String code) {
        GetChannelDataVo getChannelDataVo = channelDataService.getByCode(code);
        return Result.success(getChannelDataVo);
    }
}
