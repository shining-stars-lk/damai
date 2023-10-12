package com.example.controller;


import com.example.common.ApiResponse;
import com.example.dto.MethodChainDto;
import com.example.dto.PageDto;
import com.example.notice.ApiStatNotice;
import com.example.service.ApiStatSystemService;
import com.example.structure.MethodDetailData;
import com.example.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.example.constant.ApiStatConstant.PLATFORM_NOTICE;

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
    
    @Autowired
    private ApiStatNotice apiStatNotice;

    @PostMapping(value = "/getControllerMethods")
    public ApiResponse<List<MethodDetailData>> getControllerMethods(){
        return ApiResponse.ok(apiStatSystemService.getControllerMethods());
    }

    @PostMapping(value = "/getControllerMethodsPage")
    public ApiResponse<PageVo<MethodDetailData>> getControllerMethodsPage(@Valid @RequestBody PageDto pageDto){
        return ApiResponse.ok(apiStatSystemService.getControllerMethodsPage(pageDto));
    }

    @PostMapping(value = "/getMethodChainList")
    public ApiResponse<MethodDetailData> getMethodChainList(@Valid @RequestBody MethodChainDto methodChainDto){
        return ApiResponse.ok(apiStatSystemService.getMethodChainList(methodChainDto));
    }
    
    @RequestMapping(value = "/notice")
    public ApiResponse notice(){
        apiStatNotice.notice(PLATFORM_NOTICE);
        return ApiResponse.ok();
    }

}
