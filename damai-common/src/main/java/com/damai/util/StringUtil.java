package com.damai.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: String工具
 * @author: 阿宽不是程序员
 **/
public class StringUtil {
	private final static Logger logger = LoggerFactory.getLogger(StringUtil.class);

	/**
	 * 判断字符串不为空
	 * @param str 字符串
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return (str != null && !str.isEmpty() && !str.trim().isEmpty() && !"null".equalsIgnoreCase(str.trim())
				&& !"undefined".equalsIgnoreCase(str.trim()) && !"NULL".equalsIgnoreCase(str.trim()));

	}

	/**
	 * 判断字符串为空
	 * @param str	字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return !StringUtil.isNotEmpty(str);
	}
	
	/**
	 * 将流转换为字符串
	 * @param is 文件流
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is){
		ByteArrayOutputStream baos = null;
		String result = null;
		try {
			if(is != null) {
				baos = new ByteArrayOutputStream();
				int i = -1;
				while ((i = is.read()) != -1) {
					baos.write(i);
				}
				result = baos.toString();
			}
		}catch(IOException e) {
			throw new RuntimeException("流转换为字符串失败！");
		}finally {
			if(baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					logger.error("关闭流失败！");
				}
			}
		}
		return result;
	}
}
