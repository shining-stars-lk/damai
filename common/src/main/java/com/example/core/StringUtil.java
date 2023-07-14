package com.example.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @program: redis-tool
 * @description: String工具类
 * @author: kuan
 * @create: 2023-05-28
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
