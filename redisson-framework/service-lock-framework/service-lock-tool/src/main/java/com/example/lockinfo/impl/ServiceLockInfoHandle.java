package com.example.lockinfo.impl;

import com.example.lockinfo.AbstractLockInfoHandle;

/**
 * @program: cook-frame
 * @description: 锁业务名和标识进行组装并获取类
 * @author: 星哥
 * @create: 2023-05-28
 **/
public class ServiceLockInfoHandle extends AbstractLockInfoHandle {

    private static final String LOCK_PREFIX_NAME = "SERVICE_LOCK";
    
    @Override
    protected String getLockPrefixName() {
        return LOCK_PREFIX_NAME;
    }
}
