package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.InfoDto;
import com.damai.service.ConsumerService;
import com.damai.vo.InfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/consumer")
@Api(tags = "consumer", description = "消费者")
public class ConsumerController {
    
    @Autowired
    private ConsumerService consumerService;
    
    @ApiOperation(value = "查询信息")
    @PostMapping(value = "/getInfo")
    public ApiResponse<InfoVo> getInfo(@Valid @RequestBody InfoDto infoDto) {
        return consumerService.getInfo(infoDto);
    }
}
