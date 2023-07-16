package com.example.controller;

import com.example.dto.GetEmployeeDto;
import com.example.service.EmployeeService;
import com.example.vo.GetEmployeeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-04-20
 **/
@RestController
@RequestMapping("/employee")
@Api(tags = "employee", description = "职员")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @ApiOperation(value = "查询职员")
    @PostMapping(value = "/getEmployee")
    public GetEmployeeVo getEmployee(@RequestBody GetEmployeeDto getOrderDto) throws Exception {
        return employeeService.getEmployee(getOrderDto);
    }
    
    @ApiOperation(value = "查询职员V2")
    @PostMapping(value = "/getEmployeeV2")
    public GetEmployeeVo getEmployeeV2(@RequestBody GetEmployeeDto getOrderDto) throws Exception {
        return employeeService.getEmployeeV2(getOrderDto);
    }
}
