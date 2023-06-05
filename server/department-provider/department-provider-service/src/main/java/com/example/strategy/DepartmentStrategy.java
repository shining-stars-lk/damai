package com.example.strategy;

import com.example.dto.GetDeptDto;
import com.example.vo.GetDeptVo;

import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-06-05
 **/
public interface DepartmentStrategy {
    
    List<GetDeptVo> getDeptListByCode(GetDeptDto dto);
}
