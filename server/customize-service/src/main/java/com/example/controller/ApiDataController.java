package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.dto.ApiDataDto;
import com.example.service.ApiDataService;
import com.example.vo.ApiDataVo;
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
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/apiData")
public class ApiDataController {
    
    @Autowired
    private ApiDataService apiDataService;
    
    @RequestMapping(value = "/pageList", headers = "api-version=4.0.0", method = RequestMethod.POST)
    public Result<Page<ApiDataVo>> pageList(@Valid @RequestBody ApiDataDto dto) {
        return Result.success(apiDataService.pageList(dto));
    }
}
