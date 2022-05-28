package com.example.distributecache.distributedid;


import com.example.distributecache.core.SpringUtil;
import com.example.distributecache.core.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @program: distribute-cache
 * @description: 全局ID生成器 注：这些函数都不需要加同步机制，但数据库需要对相应的ID字段做unique约束，防止亿万分之一可能性的重复
 * @author: lk
 * @create: 2022-05-28
 **/
public class IdUtil {
	private final static Logger logger = LoggerFactory.getLogger(IdUtil.class);
	private static final String LOOP_IP = "127.0.0.1";
	private static final String HOST_HAME = "localhost";
	private static final String PADDING_ZERO_TWO = "00";
	private static final String PADDING_ZERO_ONE = "0";
	private static final int SMALL_VALUE = 10;// 自增下限
	private static final int SMALL_VALUE_FOUR = 1000;// 自增下限
	private static final int BIG_VALUE = 90;// 自增上限
	private static final int BIG_VALUE_FOUR = 9999;// 自增上限
	private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();// 线程计数器

	/**
	 * 业务ID生成器
	 * 非业务方式生成ID 13位时间戳 + 本机IP（后3位） + DATACENTER_ID(后3位) + TID(后3位) + 单毫秒内的自增序列（2位）
	 * 
	 * @return
	 */
	public static String getId() {
		String result = null;
		try {
			IdGeneratorProvider idGeneratorProvider = SpringUtil.getBean(IdGeneratorProvider.class);
			if (idGeneratorProvider == null) {
				throw new RuntimeException("idGeneratorProvider为空");
			}
			String currentMillsStr = getTimeMillis();
			String sortedNum = getRandomInt(currentMillsStr);
			String datacenterId = idGeneratorProvider.lockGetDatacenterId(currentMillsStr);
			// 如果本毫秒内无法获取到单个线程的计数，说明要么该毫秒的周期结束，要么该毫秒的周期内达到计数上限，重新迭代(相当于自旋)
			if (sortedNum == null) {
				result = getId();
			// 如果本毫秒内无法获取到单个线程的计数，说明要么该毫秒的周期结束，要么该毫秒的周期内达到计数上限，重新迭代(相当于自旋)
			}else if (StringUtil.isEmpty(datacenterId)) {
				result = getId();
			}else {
				String ipCount = getIpCount();
				String threadId = getThreadId();
				result = new StringBuffer().append(currentMillsStr).append(ipCount).append(datacenterId)
						.append(getThreadId()).append(sortedNum).toString();
				logger.info("时间戳:{} ,ip:{} ,datacenterId:{} ,threadId:{} ,sortedNum:{}",currentMillsStr,ipCount,datacenterId,threadId,sortedNum);
			}
		} catch (Exception e) {
			logger.error("全局业务ID生成出现错误，请检查您的环境: " + e.getMessage());
			logger.error(e.getMessage());
		}
		return result;
	}

	public static String getShorterId() {
		String result = null;
		try {
			String currentMillsStr = getTimeMillis();
			String sortedNum = getRandomFourInt(currentMillsStr);
			// 如果本毫秒内无法获取到单个线程的计数，说明要么该毫秒的周期结束，要么该毫秒的周期内达到计数上限，重新迭代(相当于自旋)
			if (sortedNum == null) {
				result = getShorterId();
			} else {
				result = new StringBuffer().append(currentMillsStr).append(getIpCount()).append(sortedNum).toString();
		}

	} catch (Exception e) {
			logger.error("全局业务短ID生成出现错误，请检查您的环境: " + e.getMessage());
			logger.error(e.getMessage());
		}
		return result;
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
		String result = null;
		int lastIp = Integer.parseInt(getServerIp().split("\\.")[3]);
		if (lastIp < 10) {
			result = PADDING_ZERO_TWO + lastIp;
		} else if (lastIp < 100) {
			result = PADDING_ZERO_ONE + lastIp;
		} else {
			result = String.valueOf(lastIp);
		}
		return result;
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
			String threadIdStr = String.valueOf(processId);
			resultId = threadIdStr.substring(threadIdStr.length() - 3);
		}

		return resultId;
	}

	/**
	 * 获取线程ID的后3位
	 * 
	 * @return
	 */
	private static String getThreadId() {
		String resultId = null;
		long threadId = Thread.currentThread().getId();
		if (threadId < 10) {
			resultId = PADDING_ZERO_TWO + threadId;
		} else if (threadId < 100) {
			resultId = PADDING_ZERO_ONE + threadId;
		} else if (threadId < 1000) {
			resultId = String.valueOf(threadId);
		} else {
			String threadIdStr = String.valueOf(threadId);
			resultId = threadIdStr.substring(threadIdStr.length() - 3);
		}

		return resultId;
	}

	/**
	 * 单毫秒内的自增序列(如果同一线程同一毫秒内超过了自增上限99，则自旋到下一个毫秒周期获取) 因此一个线程在一毫秒内可以生产出90个ID值
	 * 
	 * @return
	 */
	private static String getRandomInt(String currentMillsStr) {
		String result = null;
		String value = threadLocal.get();
		long currentMills = Long.parseLong(currentMillsStr);
		if (value == null) {// 如果为空表示线程计数器，在某一毫秒的初始周期
			result = String.valueOf(SMALL_VALUE);
			threadLocal.set(currentMills + "_" + String.valueOf(SMALL_VALUE + 1));
		} else {
			long oldMillis = Long.parseLong(value.split("_")[0]);
			int increValue = Integer.parseInt(value.split("_")[1]);

			if (oldMillis < currentMills) {// 如果当前时间大于计数器时间，则代表计数器的这一个毫秒的周期结束,函数返回值只能为空，让外层函数重新开始下一个周期
				threadLocal.remove();
			} else {
				if (increValue <= BIG_VALUE) {// 如果小于上限值，则本周期内继续做累加
					threadLocal.set(currentMills + "_" + String.valueOf(increValue + 1));
					result = String.valueOf(increValue);
				}
			}
		}
		return result;
	}

	/**
	 * 单毫秒内的自增序列(如果同一线程同一毫秒内超过了自增上限99，则自旋到下一个毫秒周期获取) 因此一个线程在一毫秒内可以生产出9000个ID值
	 *
	 * @return
	 */
	private static String getRandomFourInt(String currentMillsStr) {
		String result = null;
		String value = threadLocal.get();
		long currentMills = Long.parseLong(currentMillsStr);
		if (value == null) {// 如果为空表示线程计数器，在某一毫秒的初始周期
			result = String.valueOf(SMALL_VALUE_FOUR);
			threadLocal.set(currentMills + "_" + String.valueOf(SMALL_VALUE_FOUR + 1));
		} else {
			long oldMillis = Long.parseLong(value.split("_")[0]);
			int increValue = Integer.parseInt(value.split("_")[1]);

			if (oldMillis < currentMills) {// 如果当前时间大于计数器时间，则代表计数器的这一个毫秒的周期结束,函数返回值只能为空，让外层函数重新开始下一个周期
				threadLocal.remove();
			} else {
				if (increValue <= BIG_VALUE_FOUR) {// 如果小于上限值，则本周期内继续做累加
					threadLocal.set(currentMills + "_" + String.valueOf(increValue + 1));
					result = String.valueOf(increValue);
				}
			}
		}
		return result;
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
			Enumeration<InetAddress> addresses = ni.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress addr = addresses.nextElement();

				if (addr instanceof Inet4Address) {
					String ipStr = addr.getHostAddress().trim();
					if (!LOOP_IP.equalsIgnoreCase(ipStr) && !HOST_HAME.equalsIgnoreCase(ipStr)) {
						return ipStr;
					}
					// logger.error("网卡接口名称：" + ni.getName());
					// logger.error("网卡接口地址：" + addr.getHostAddress());
				}
			}
		}

		return LOOP_IP;
	}


	
	public static void main(String[] args) {
		for(int i=0;i<10;i++) {
			System.out.println(IdUtil.getId());
		}
		
	}
}
