package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Rule;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-30
 **/
public interface RuleMapper extends BaseMapper<Rule> {
    
    int delAll();
}
