package com.example.toolkit;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-02-02
 **/
public class IdGeneratorConstant {
    
    /**
     * 时间起始标记点，作为基准，一般取系统的最近时间（一旦确定不能变动）
     */
    public static final long TWEPOCH = 1288834974657L;
    /**
     * 机器标识位数
     */
    public static final long WORKER_ID_BITS = 5L;
    public static final long DATA_CENTER_ID_BITS = 5L;
    public static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
    public static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);
    /**
     * 毫秒内自增位
     */
    public static final long SEQUENCE_BITS = 12L;
    public static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    public static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    /**
     * 时间戳左移动位
     */
    public static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    public static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);
}
