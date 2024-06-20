package com.damai.lockinfo.factory;


import com.damai.lockinfo.LockInfoHandle;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 锁信息工厂
 * @author: 阿星不是程序员
 **/
public class LockInfoHandleFactory implements ApplicationContextAware {
    
    private ApplicationContext applicationContext;

    public LockInfoHandle getLockInfoHandle(String lockInfoType){
        return applicationContext.getBean(lockInfoType,LockInfoHandle.class);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
