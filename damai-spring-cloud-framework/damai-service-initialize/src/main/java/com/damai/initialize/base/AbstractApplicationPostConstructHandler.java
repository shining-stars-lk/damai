package com.damai.initialize.base;

import jakarta.annotation.PostConstruct;

import static com.damai.initialize.constant.InitializeHandlerType.APPLICATION_POST_CONSTRUCT;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用于处理 {@link PostConstruct} 类型 初始化执行 抽象
 * @author: 阿星不是程序员
 **/
public abstract class AbstractApplicationPostConstructHandler implements InitializeHandler {
    
    @Override
    public String type() {
        return APPLICATION_POST_CONSTRUCT;
    }
}
