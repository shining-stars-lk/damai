package com.tool.multiplesubmitlimit.info.strategy.generateKey;

import org.aspectj.lang.JoinPoint;

/**
 * @program: distribute-cache
 * @description: 生成键策略接口
 * @author: k
 * @create: 2022-05-28
 **/
public interface GenerateKeyHandler {

    String generateKey(JoinPoint joinPoint);
}
