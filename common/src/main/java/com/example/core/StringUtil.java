package com.example.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: distribute-cache
 * @description: String工具类
 * @author: lk
 * @create: 2022-05-28
 **/
public class StringUtil {
	private final static Logger logger = LoggerFactory.getLogger(StringUtil.class);

	/**
	 * 判断字符串不为空
	 * @param str 字符串
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return (str != null && !str.equals("") && str.trim().length() > 0 && !str.trim().equalsIgnoreCase("null")
				&& !str.trim().equalsIgnoreCase("undefined") && !str.trim().equalsIgnoreCase("NULL"));

	}

	/**
	 * 判断字符串为空
	 * @param str	字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return !StringUtil.isNotEmpty(str);
	}
}
