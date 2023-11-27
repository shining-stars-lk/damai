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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Service
@Slf4j
public class EmployeeService {
    
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,2,30, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10));
    
    @Autowired
    private DepartmentClient departmentClient;
    
    public GetEmployeeVo getEmployee(final GetEmployeeDto getEmployeeDto) throws Exception {
        GetEmployeeVo getEmployeeVo = new GetEmployeeVo();
        getEmployeeVo.setId(getEmployeeDto.getId());
        getEmployeeVo.setName("苹果员工-1");
        
        GetDepartmentDto getDepartmentDto = new GetDepartmentDto();
        getDepartmentDto.setId(getEmployeeVo.getId());
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
    
    public boolean testAgent() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("任务执行");
            }
        });
        thread.start();
        
        threadPoolExecutor.execute(() -> System.out.println("线程池执行"));
        return true;
    }
}
