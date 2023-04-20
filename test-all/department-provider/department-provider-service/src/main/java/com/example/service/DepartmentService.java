package com.example.service;

import com.alibaba.fastjson.JSON;
import com.example.dto.GetDepartmentDto;
import com.example.vo.GetDepartmentVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Service
@Log4j2
public class DepartmentService {
    
    
    private Integer number1 = 10000;
    
    private Integer number2 = 999;
    
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
}
