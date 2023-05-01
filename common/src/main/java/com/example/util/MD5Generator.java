package com.example.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: MD5Generator
 * @Description: MD5算法类
 * @author Joran
 * @date 24 Jul 2014 15:48:27
 * 
 */
@Slf4j
public class MD5Generator {

	/**
	 * @Title: MD5
	 * @Description: MD5加密算法
	 * @param @param sourceStr 要被加密的字符串
	 * @return String 返回加密后的字符串
	 * @throws
	 */
	public static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			// 将错误记录到日志中
			log.error(String.valueOf(e.getCause()));
		}
		return result;
	}

	/**
	 * @Title: encryptPassword
	 * @Description: 加盐密码的加密和验证算法
	 * @param password
	 *            密码值
	 * @param salt
	 *            密码加密的盐值
	 * @return String 返回加密后的密码值
	 * @throws
	 */
	public static String encryptPassword(String password, String salt) {
		// salt为null时代表新用户注册或用户修改密码时需要生成盐值，不为null时代表登录验证密码
		if (salt == null) {
			// 生成随机8位盐值
			salt = RandomStringGenerator.gen8DigitalString();
		}
		// 给第一次MD5值加盐（盐值与前台传回的第一次MD5密码进行组合）
		String tempPsw = salt + password;
		// 第二次MD5运算
		tempPsw = MD5Generator.MD5(tempPsw);
		// 生成最终加密密码，前8位是盐值，后面是第二次MD5运算值
//		tempPsw = salt + tempPsw;
		// 返回加密后的密码
		return tempPsw;
	}

}
