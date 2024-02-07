package com.example.lockinfo.impl;

import com.example.lockinfo.AbstractLockInfoHandle;

/**
 * @program: redis-example
 * @description: 防重复提交标识组装类
 * @author: 星哥
 * @create: 2023-05-28
 **/
public class RepeatExecuteLimitLockInfoHandle extends AbstractLockInfoHandle {

    public static final String PREFIX_NAME = "REPEAT_EXECUTE_LIMIT";
    
    @Override
    protected String getLockPrefixName() {
        return PREFIX_NAME;
    }
}
