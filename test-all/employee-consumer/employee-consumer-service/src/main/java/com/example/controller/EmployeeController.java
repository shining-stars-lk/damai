package com.example.controller;

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
    
    @PostMapping(value = "/getEmployee")
    public GetEmployeeVo getEmployee(@RequestBody GetEmployeeDto getOrderDto){
        return employeeService.getEmployee(getOrderDto);
    }
    
    @PostMapping(value = "/getEmployeeV2")
    public GetEmployeeVo getEmployeeV2(@RequestBody GetEmployeeDto getOrderDto){
        return employeeService.getEmployeeV2(getOrderDto);
    }
}
