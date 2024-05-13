package com.damai.toolkit;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 雪花算法相关常量
 * @author: 阿星不是程序员
 **/
public class IdGeneratorConstant {
    /**
     * 机器标识位数
     */
    public static final long WORKER_ID_BITS = 5L;
    public static final long DATA_CENTER_ID_BITS = 5L;
    public static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
    public static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);
}
