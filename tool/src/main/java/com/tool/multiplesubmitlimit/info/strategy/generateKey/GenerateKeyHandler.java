package com.tool.multiplesubmitlimit.info.strategy.generateKey;

import org.aspectj.lang.JoinPoint;

/**
 * @program: redis-tool
 * @description: 生成键策略接口
 * @author: kuan
 * @create: 2023-05-28
 **/
public interface GenerateKeyHandler {

    String generateKey(JoinPoint joinPoint);
}
