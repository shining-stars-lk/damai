package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.DepthRule;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-30
 **/
public interface DepthRuleMapper extends BaseMapper<DepthRule> {
    
    int delAll();
}
