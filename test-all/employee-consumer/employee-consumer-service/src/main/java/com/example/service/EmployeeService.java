package com.example.service;

import com.alibaba.fastjson.JSON;
import com.example.client.DepartmentClient;
import com.example.dto.GetDepartmentDto;
import com.example.dto.GetEmployeeDto;
import com.example.vo.GetDepartmentVo;
import com.example.vo.GetEmployeeVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Service
@Log4j2
public class EmployeeService {
    
    @Autowired
    private DepartmentClient departmentClient;
    
    public GetEmployeeVo getEmployee(final GetEmployeeDto getEmployeeDto) throws Exception {
        GetEmployeeVo getEmployeeVo = new GetEmployeeVo();
        getEmployeeVo.setId(getEmployeeDto.getId());
        getEmployeeVo.setName("苹果员工-1");
        
        GetDepartmentDto getDepartmentDto = new GetDepartmentDto();
        getDepartmentDto.setId(getEmployeeVo.getId() + "11");
        if (getEmployeeDto.getSleepTime() != null) {
            getDepartmentDto.setSleepTime(getEmployeeDto.getSleepTime());
        }
        GetDepartmentVo getVo = departmentClient.get(getDepartmentDto);
        if (getVo != null) {
            getEmployeeVo.setDepartmentId(getVo.getId());
            getEmployeeVo.setDepartmentName(getVo.getName());
        }
        log.info("getEmployee执行 GetDepartmentDto : {}", JSON.toJSONString(getEmployeeVo));
        return getEmployeeVo;
    }
    
    public GetEmployeeVo getEmployeeV2(final GetEmployeeDto getEmployeeDto) throws Exception {
        GetEmployeeVo getEmployeeVo = new GetEmployeeVo();
        getEmployeeVo.setId(getEmployeeDto.getId());
        getEmployeeVo.setName("橘子员工-1");
        
        GetDepartmentDto getDepartmentDto = new GetDepartmentDto();
        getDepartmentDto.setId(getEmployeeVo.getId() + "22");
        if (getDepartmentDto.getSleepTime() != null) {
            getDepartmentDto.setSleepTime(getEmployeeDto.getSleepTime());
        }
        GetDepartmentVo getVo = departmentClient.get(getDepartmentDto);
        if (getVo != null) {
            getEmployeeVo.setDepartmentId(getVo.getId());
            getEmployeeVo.setDepartmentName(getVo.getName());
        }
        log.info("getEmployeeV2执行 GetDepartmentDto : {}", JSON.toJSONString(getEmployeeVo));
        return getEmployeeVo;
    }
}
