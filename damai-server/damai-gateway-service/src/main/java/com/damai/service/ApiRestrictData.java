package com.damai.service;

import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 接口请求记录 实体对象
 * @author: 阿星不是程序员
 **/
@Data
public class ApiRestrictData {

    private Long triggerResult;
    
    private Long triggerCallStat;
    
    private Long apiCount;
    
    private Long threshold;
    
    private Long messageIndex;
}
