package com.example.lockinfo.factory;


import com.example.lockinfo.LockInfoHandle;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @program: redis-example
 * @description: 锁信息提供工厂
 * @author: 星哥
 * @create: 2023-05-28
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
