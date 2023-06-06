package com.example.controller;

import com.example.common.Result;
import com.example.dto.GetDepartmentDto;
import com.example.dto.GetDeptDto;
import com.example.service.DepartmentService;
import com.example.vo.GetDepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/department")
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    @PostMapping(value = "/get")
    public GetDepartmentVo get(@RequestBody GetDepartmentDto dto){
        return departmentService.get(dto);
    }
    
    @PostMapping(value = "/getV2")
    public GetDepartmentVo getV2(@RequestBody GetDepartmentDto dto){
        return departmentService.getV2(dto);
    }
    
    @PostMapping(value = "/printTraceId")
    public Result printTraceId(){
        return Result.success(departmentService.printTraceId());
    }
    
    @PostMapping(value = "/getDeptListByCode")
    public Result getDeptListByCode(@RequestBody GetDeptDto dto){
        return Result.success(departmentService.getDeptListByCode(dto));
    } 
}
