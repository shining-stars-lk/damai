package com.example.client;

import com.example.dto.GetDepartmentDto;
import com.example.vo.GetDepartmentVo;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Component
public class DepartmentClientFallback implements DepartmentClient {
    @Override
    public GetDepartmentVo get(final GetDepartmentDto dto) {
       return null;
    }
    
    @Override
    public GetDepartmentVo getV2(final GetDepartmentDto dto) {
        return null;
    }
}
