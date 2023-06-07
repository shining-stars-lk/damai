package com.example.client;

import com.example.dto.GetDepartmentDto;
import com.example.vo.GetDepartmentVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Component
@FeignClient(value = "department-provider-service",fallback = DepartmentClientFallback.class)
public interface DepartmentClient {
    
    @PostMapping("/department/get")
    GetDepartmentVo get(GetDepartmentDto dto) throws Exception;
    
    @PostMapping("/department/getV2")
    GetDepartmentVo getV2(GetDepartmentDto dto) throws Exception;
}
