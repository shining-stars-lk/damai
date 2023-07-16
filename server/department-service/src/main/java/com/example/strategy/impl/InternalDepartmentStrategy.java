package com.example.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dto.GetDeptDto;
import com.example.entity.Department;
import com.example.mapper.DepartmentMapper;
import com.example.strategy.DepartmentStrategy;
import com.example.strategy.factory.DepartmentFactory;
import com.example.vo.GetDeptVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: toolkit
 * @description: 内部数据库获取部门列表策略
 * @author: 星哥
 * @create: 2023-06-05
 **/
@Component
public class InternalDepartmentStrategy implements DepartmentStrategy {
    private static final String code = "0001";
    
    @Autowired
    private DepartmentMapper departmentMapper;
    
    @PostConstruct
    public void init(){
        DepartmentFactory.register(code,this);
    }
    @Override
    public List<GetDeptVo> getDeptListByCode(final GetDeptDto dto) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_code",dto.getTypeCode());
        List<Department> departments = departmentMapper.selectList(queryWrapper);
        List<GetDeptVo> GetDeptVos = departments.stream().map(department -> {
            GetDeptVo getDeptVo = new GetDeptVo();
            BeanUtils.copyProperties(department, getDeptVo);
            return getDeptVo;
        }).collect(Collectors.toList());
        return GetDeptVos;
    }
}
