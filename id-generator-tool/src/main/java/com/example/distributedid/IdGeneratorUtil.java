package com.example.distributedid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局ID生成器
 * 注：这些函数都不需要加同步机制，但数据库需要对相应的ID字段做unique约束，防止亿万分之一可能性的重复
 *
 * @author 星哥
 */
public class IdGeneratorUtil {
    private final static Logger logger = LoggerFactory.getLogger(IdGeneratorUtil.class);
    private static final String LOOP_IP = "127.0.0.1";
    private static final String HOST_HAME = "localhost";
    private static final String PADDING_ZERO_TWO = "00";
    private static final String PADDING_ZERO_ONE = "0";
    /**
     * 自增下限
     */
    private static final int SMALL_VALUE = 10;
    /**
     * 自增下限
     */
    private static final int SMALL_VALUE_FOUR = 1000;
    /**
     * 自增上限
     */
    private static final int BIG_VALUE = 90;
    /**
     * 自增上限
     */
    private static final int BIG_VALUE_FOUR = 9999;
    /**
     * 记录最近一次获取id时的时间
     */
    private static long lastTimestamp = -1L;
    /**
     * 允许的时钟回拨毫秒数
     */
    private static final long timeOffset = 0L;
    /**
     * 线程计数器
     */
    private static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<>();
    /**
     * id类型的threadLoca标识符
     */
    private static String ID_TAG = "id_tag";
    /**
     * shortId类型的threadLoca标识符
     */
    private static String SHORT_ID_TAG = "short_id_tag";
    
    
    /**
     * 业务ID生成器
     * 非业务方式生成ID 13位时间戳 + 本机IP（后3位） + PID(后3位) + TID(后3位) + 单毫秒内的自增序列（2位）
     *
     * @return
     */
    public static String getId() {
        String result = null; try {
            String currentMillsStr = getTimeMillis(); String sortedNum = getRandomInt(currentMillsStr);
            // 如果本毫秒内无法获取到单个线程的计数，说明要么该毫秒的周期结束，要么该毫秒的周期内达到计数上限，重新迭代(相当于自旋)
            if (sortedNum == null) {
                result = getId();
            } else {
                result = new StringBuffer().append(currentMillsStr).append(getIpCount()).append(getProcessId()).append(getThreadId()).append(sortedNum).toString();
            }
            
        } catch (Exception e) {
            logger.error("全局业务ID生成出现错误，请检查您的环境: " + e.getMessage()); logger.error(e.getMessage());
        } return result;
    }
    
    /**
     * 获取当前时间的毫秒数
     *
     * @return
     */
    private static String getTimeMillis() {
        return String.valueOf(System.currentTimeMillis());
    }
    
    /**
     * 获取本机内网IP地址的后3位
     *
     * @return
     */
    private static String getIpCount() throws Exception {
        String result = null; int lastIp = Integer.parseInt(getServerIp().split("\\.")[3]); if (lastIp < 10) {
            result = PADDING_ZERO_TWO + lastIp;
        } else if (lastIp < 100) {
            result = PADDING_ZERO_ONE + lastIp;
        } else {
            result = String.valueOf(lastIp);
        } return result;
    }
    
    /**
     * 获取进程ID的后3位
     *
     * @return
     */
    private static String getProcessId() {
        String resultId = null;
        long processId = Long.parseLong(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        if (processId < 10) {
            resultId = PADDING_ZERO_TWO + processId;
        } else if (processId < 100) {
            resultId = PADDING_ZERO_ONE + processId;
        } else if (processId < 1000) {
            resultId = String.valueOf(processId);
        } else {
            String threadIdStr = String.valueOf(processId); resultId = threadIdStr.substring(threadIdStr.length() - 3);
        }
        
        return resultId;
    }
    
    /**
     * 获取线程ID的后3位
     *
     * @return
     */
    private static String getThreadId() {
        String resultId = null; long threadId = Thread.currentThread().getId(); if (threadId < 10) {
            resultId = PADDING_ZERO_TWO + threadId;
        } else if (threadId < 100) {
            resultId = PADDING_ZERO_ONE + threadId;
        } else if (threadId < 1000) {
            resultId = String.valueOf(threadId);
        } else {
            String threadIdStr = String.valueOf(threadId); resultId = threadIdStr.substring(threadIdStr.length() - 3);
        }
        
        return resultId;
    }
    
    /**
     * 单毫秒内的自增序列(如果同一线程同一毫秒内超过了自增上限99，则自旋到下一个毫秒周期获取) 因此一个线程在一毫秒内可以生产出90个ID值
     *
     * @return
     */
    private synchronized static String getRandomInt(String currentMillsStr) {
        
        long currentMills = Long.parseLong(currentMillsStr);
        //记录是否发生时钟回拨
        if (currentMills < lastTimestamp) {
            if (lastTimestamp - currentMills < timeOffset) {
                // 容忍指定的回拨，避免NTP校时造成的异常
                currentMills = lastTimestamp;
            } else {
                // 如果服务器时间有问题(时钟后退) 报错。
                logger.warn("发生时钟回拨 lastTimestamp:{} ,currentMills:{}", lastTimestamp, currentMills);
                //发生时钟回拨直接返回null，然后再次进行获取
                return null;
            }
        } String result = null; Map<String, String> map = threadLocal.get(); if (map == null) {
            map = new HashMap<>(2);
        }
        // 如果为空表示线程计数器，在某一毫秒的初始周期
        String value = map.get(ID_TAG); if (value == null) {
            result = String.valueOf(SMALL_VALUE); map.put(ID_TAG, currentMills + "_" + String.valueOf(SMALL_VALUE + 1));
            threadLocal.set(map);
        } else {
            long oldMillis = Long.parseLong(value.split("_")[0]);
            int increValue = Integer.parseInt(value.split("_")[1]);
            
            // 如果当前时间大于计数器时间，则代表计数器的这一个毫秒的周期结束,函数返回值只能为空，让外层函数重新开始下一个周期
            if (oldMillis < currentMills) {
                threadLocal.remove();
            } else {
                // 如果小于上限值，则本周期内继续做累加
                if (increValue <= BIG_VALUE) {
                    map.put(ID_TAG, currentMills + "_" + String.valueOf(increValue + 1)); threadLocal.set(map);
                    result = String.valueOf(increValue);
                }
            }
        } lastTimestamp = currentMills; return result;
    }
    
    /**
     * 获取服务器内网IP地址
     *
     * @return
     */
    public static String getServerIp() throws Exception {
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> addresses = ni.getInetAddresses(); while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                
                if (addr instanceof Inet4Address) {
                    String ipStr = addr.getHostAddress().trim();
                    if (!LOOP_IP.equalsIgnoreCase(ipStr) && !HOST_HAME.equalsIgnoreCase(ipStr)) {
                        return ipStr;
                    }
                }
            }
        }
        
        return LOOP_IP;
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(IdGeneratorUtil.getId());
        }
    }
}
