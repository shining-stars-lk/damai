package com.example.controller;


import com.example.dto.PageDto;
import com.example.service.ApiStatSystemService;
import com.example.structure.MethodDetailData;
import com.example.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

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
    public Set<MethodDetailData> getControllerMethods(){
        return apiStatSystemService.getControllerMethods();
    }

    @PostMapping(value = "/getControllerMethodsPage")
    public PageVo<MethodDetailData> getControllerMethodsPage(@RequestBody PageDto pageDto){
        return apiStatSystemService.getControllerMethodsPage(pageDto);
    }

    @PostMapping(value = "/getMethodChainList")
    public MethodDetailData getMethodChainList(String controllerMethod){
        return apiStatSystemService.getMethodChainList(controllerMethod);
    }

}
