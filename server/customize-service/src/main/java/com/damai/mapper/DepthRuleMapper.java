package com.damai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.damai.entity.DepthRule;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 深度规则 mapper
 * @author: 阿宽不是程序员
 **/
public interface DepthRuleMapper extends BaseMapper<DepthRule> {
    
    int delAll();
}
