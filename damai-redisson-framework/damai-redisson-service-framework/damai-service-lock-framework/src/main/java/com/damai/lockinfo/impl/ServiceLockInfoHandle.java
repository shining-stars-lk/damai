package com.damai.lockinfo.impl;

import com.damai.lockinfo.AbstractLockInfoHandle;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 锁信息实现(分布式锁)
 * @author: 阿星不是程序员
 **/
public class ServiceLockInfoHandle extends AbstractLockInfoHandle {

    private static final String LOCK_PREFIX_NAME = "SERVICE_LOCK";
    
    @Override
    protected String getLockPrefixName() {
        return LOCK_PREFIX_NAME;
    }
}
