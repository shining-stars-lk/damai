package com.example.multiplesubmitlimit.info.strategy.generateKey;

import org.aspectj.lang.JoinPoint;

/**
 * @program: redis-example
 * @description: 生成键策略接口
 * @author: 星哥
 * @create: 2023-05-28
 **/
public interface GenerateKeyHandler {

    String generateKey(JoinPoint joinPoint);
}
