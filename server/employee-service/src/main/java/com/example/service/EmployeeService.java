package com.example.service;

import com.alibaba.fastjson.JSON;
import com.example.annotation.JobCall;
import com.example.client.DepartmentClient;
import com.example.dto.GetDepartmentDto;
import com.example.dto.GetEmployeeDto;
import com.example.vo.GetDepartmentVo;
import com.example.vo.GetEmployeeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Service
@Slf4j
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
    
    @JobCall
    public GetEmployeeVo getEmployeeV2(final GetEmployeeDto getEmployeeDto) throws Exception {
        GetEmployeeVo getEmployeeVo = new GetEmployeeVo();
        getEmployeeVo.setId(getEmployeeDto.getId());
        getEmployeeVo.setName("橘子员工-1");
        log.info("getEmployeeV2执行 GetDepartmentDto : {}", JSON.toJSONString(getEmployeeVo));
        return getEmployeeVo;
    }
}
