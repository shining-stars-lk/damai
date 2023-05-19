package com.example.controller;

import com.example.common.Result;
import com.example.dto.GetEmployeeDto;
import com.example.service.EmployeeService;
import com.example.vo.GetEmployeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-20
 **/
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @PostMapping(value = "/test")
    public Result test() throws Exception {
        Thread.sleep(100);
        return Result.success(true);
    }
    
    @PostMapping(value = "/getEmployee")
    public GetEmployeeVo getEmployee(@RequestBody GetEmployeeDto getOrderDto) throws Exception {
        return employeeService.getEmployee(getOrderDto);
    }
    
    @PostMapping(value = "/getEmployeeV2")
    public GetEmployeeVo getEmployeeV2(@RequestBody GetEmployeeDto getOrderDto) throws Exception {
        return employeeService.getEmployeeV2(getOrderDto);
    }
}
