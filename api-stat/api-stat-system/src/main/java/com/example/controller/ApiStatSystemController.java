package com.example.controller;


import com.example.dto.MethodChainDto;
import com.example.dto.PageDto;
import com.example.service.ApiStatSystemService;
import com.example.structure.MethodDetailData;
import com.example.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-27
 **/
@RestController
public class ApiStatSystemController {

    @Autowired
    private ApiStatSystemService apiStatSystemService;

    @PostMapping(value = "/getControllerMethods")
    public List<MethodDetailData> getControllerMethods(){
        return apiStatSystemService.getControllerMethods();
    }

    @PostMapping(value = "/getControllerMethodsPage")
    public PageVo<MethodDetailData> getControllerMethodsPage(@Valid @RequestBody PageDto pageDto){
        return apiStatSystemService.getControllerMethodsPage(pageDto);
    }

    @PostMapping(value = "/getMethodChainList")
    public MethodDetailData getMethodChainList(@Valid @RequestBody MethodChainDto methodChainDto){
        return apiStatSystemService.getMethodChainList(methodChainDto);
    }

}
