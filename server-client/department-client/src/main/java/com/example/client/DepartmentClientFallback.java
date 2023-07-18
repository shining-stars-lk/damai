package com.example.client;

import com.example.dto.GetDepartmentDto;
import com.example.vo.GetDepartmentVo;
import org.springframework.stereotype.Component;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Component
public class DepartmentClientFallback implements DepartmentClient {
    @Override
    public GetDepartmentVo get(final GetDepartmentDto dto) throws Exception {
        throw new Exception("get熔断");
    }
    
    @Override
    public GetDepartmentVo getV2(final GetDepartmentDto dto) throws Exception {
        throw new Exception("getV2熔断");
    }
}
