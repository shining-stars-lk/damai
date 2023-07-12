package com.example.controller;

import com.example.common.Result;
import com.example.dto.GetDepartmentDto;
import com.example.dto.GetDeptDto;
import com.example.service.DepartmentService;
import com.example.vo.GetDepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/department")
@Api(tags = "department", description = "部门")
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    @ApiOperation(value = "查询部门")
    @PostMapping(value = "/get")
    public GetDepartmentVo get(@Valid @RequestBody GetDepartmentDto dto){
        return departmentService.get(dto);
    }
    
    @ApiOperation(value = "查询部门V2")
    @PostMapping(value = "/getV2")
    public GetDepartmentVo getV2(@RequestBody GetDepartmentDto dto){
        return departmentService.getV2(dto);
    }
    
    @ApiOperation(value = "返回链路id")
    @PostMapping(value = "/printTraceId")
    public Result printTraceId(){
        return Result.success(departmentService.printTraceId());
    }
    
    @ApiOperation(value = "通过code查询部门列表")
    @PostMapping(value = "/getDeptListByCode")
    public Result getDeptListByCode(@Valid @RequestBody GetDeptDto dto){
        return Result.success(departmentService.getDeptListByCode(dto));
    } 
}
