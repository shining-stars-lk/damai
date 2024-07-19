package com.damai.mq.callback;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用于执行成功的情况
 * @author: 阿星不是程序员
 **/
@FunctionalInterface
public interface SuccessCallback<T> {
    
    /**
     * 执行逻辑
     * @param result 执行成功的结果当做参数传递
     * */
    void onSuccess(T result);

}
