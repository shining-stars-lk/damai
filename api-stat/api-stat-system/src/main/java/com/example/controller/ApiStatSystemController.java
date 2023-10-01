package com.example.controller;


import com.example.dto.ControllerMethodPage;
import com.example.service.ApiStatSystemService;
import com.example.structure.MethodDetailData;
import com.example.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
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
    public Page<MethodDetailData> getControllerMethodsPage(@RequestBody ControllerMethodPage controllerMethodPage){
        return apiStatSystemService.getControllerMethodsPage(controllerMethodPage);
    }

    @PostMapping(value = "/getMethodChainList")
    public MethodDetailData getMethodChainList(String controllerMethod){
        return apiStatSystemService.getMethodChainList(controllerMethod);
    }

}
