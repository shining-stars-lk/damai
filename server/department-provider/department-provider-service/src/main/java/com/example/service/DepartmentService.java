package com.example.service;

import com.alibaba.fastjson.JSON;
import com.example.BusinessThreadPool;
import com.example.dto.GetDepartmentDto;
import com.example.dto.GetDeptDto;
import com.example.strategy.DepartmentStrategy;
import com.example.strategy.factory.DepartmentFactory;
import com.example.vo.GetDepartmentVo;
import com.example.vo.GetDeptVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Service
@Slf4j
public class DepartmentService {
    
    public GetDepartmentVo get(GetDepartmentDto dto){
        if (dto.getSleepTime() != null) {
            try {
                Thread.sleep(dto.getSleepTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        GetDepartmentVo getVo = new GetDepartmentVo();
        getVo.setId(dto.getId());
        getVo.setName("苹果公司");
        log.info("get执行 GetVo : {}", JSON.toJSONString(getVo));
        return getVo;
    }
    
    public GetDepartmentVo getV2(GetDepartmentDto dto){
        if (dto.getSleepTime() != null) {
            try {
                Thread.sleep(dto.getSleepTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        GetDepartmentVo getVo = new GetDepartmentVo();
        getVo.setId(dto.getId());
        getVo.setName("橘子公司");
        log.info("get执行 GetVo : {}", JSON.toJSONString(getVo));
        return getVo;
    }
    
    public List<GetDeptVo> getDeptListByCode(GetDeptDto dto) {
        DepartmentStrategy departmentStrategy = DepartmentFactory.getDepartmentStrategy(dto.getTypeCode());
        return departmentStrategy.getDeptListByCode(dto);
    }
    
    public String printTraceId() {
        log.info("printTraceId");
        BusinessThreadPool.execute(() -> log.info("async printTraceId"));
        return null;
    }
}
