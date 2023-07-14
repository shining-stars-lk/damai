package com.example.strategy.impl;

import com.example.dto.GetDeptDto;
import com.example.strategy.DepartmentStrategy;
import com.example.strategy.factory.DepartmentFactory;
import com.example.vo.GetDeptVo;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: toolkit
 * @description: 调用第三方获取部门列表策略
 * @author: kuan
 * @create: 2023-06-05
 **/
@Component
public class ThirdPartyDepartmentStrategy implements DepartmentStrategy {
    
    private static final String code = "0010";
    
    @PostConstruct
    public void init(){
        DepartmentFactory.register(code,this);
    }
    @Override
    public List<GetDeptVo> getDeptListByCode(final GetDeptDto dto) {
        //模拟调用三方资源，例如http、websocket、netty等调用方式
        List<GetDeptVo> getDeptVos = new ArrayList<>();
        GetDeptVo getDeptVo = new GetDeptVo();
        getDeptVo.setId("1");
        getDeptVo.setName("信息科");
        getDeptVo.setTypeCode(code);
        getDeptVo.setStatus(1);
        getDeptVo.setCreateTime(new Date());
        getDeptVos.add(getDeptVo);
        return getDeptVos;
    }
}
