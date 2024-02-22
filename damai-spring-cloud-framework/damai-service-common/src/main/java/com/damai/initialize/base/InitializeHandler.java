package com.damai.initialize.base;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 初始化执行 抽象
 * @author: 阿宽不是程序员
 **/
public interface InitializeHandler {
    
    String type();
    
    public Integer executeOrder();
    void executeInit();
    
}
