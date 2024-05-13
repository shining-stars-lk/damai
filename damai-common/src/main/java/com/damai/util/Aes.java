package com.damai.util;

import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: aes
 * @author: 阿星不是程序员
 **/
@Slf4j
public class Aes {
	
	private final static String AES = "AES";
	
	private final static String ENCODE_UTF_8 = "UTF-8";
	
	
	/**
	 * 根据键值进行加密
	 * @param key
	 * @param initVector
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String key, String initVector, String data) {
		try {
			byte[] bt = encrypt(key.getBytes(StandardCharsets.UTF_8), initVector.getBytes(StandardCharsets.UTF_8), data.getBytes(StandardCharsets.UTF_8));
			String strs = Base64.encode(bt);
			return strs;
		}catch (Exception e) {
			throw new DaMaiFrameException(BaseCode.AES_ERROR);
		}
		
	}
		
	/**
	 * 根据键值进行解密
	 * @param key
	 * @param initVector
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String key, String initVector, String data){
		try {
			if (data == null) {
				return null;
			}
			byte[] buf = cn.hutool.core.codec.Base64.decode(data);
			byte[] bt = decrypt(key.getBytes(StandardCharsets.UTF_8), initVector.getBytes(StandardCharsets.UTF_8), buf);
			return new String(bt, StandardCharsets.UTF_8);
		}catch (Exception e) {
			throw new DaMaiFrameException(BaseCode.AES_ERROR);
		}
		
	}
	
	
	/**
	 * 加密
	 * @param key
	 * @param initVector
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] key, byte[] initVector, byte[] data) throws Exception {
		// 注意，为了能与 iOS 统一
		// 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
		IvParameterSpec iv = new IvParameterSpec(initVector);
		SecretKeySpec secretkeySpec = new SecretKeySpec(key, AES);
		
		// 指定加密的算法、工作模式和填充方式
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, secretkeySpec, iv);
		
		return cipher.doFinal(data);
	}
	
	/**
	 * 解密
	 * @param key
	 * @param initVector
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] key, byte[] initVector, byte[] data) throws Exception {
		// 注意，为了能与 iOS 统一
		// 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
		IvParameterSpec iv = new IvParameterSpec(initVector);
		SecretKeySpec secretkeySpec = new SecretKeySpec(key, AES);
		
		// 指定加密的算法、工作模式和填充方式
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, secretkeySpec, iv);
		
		return cipher.doFinal(data);
	}
	
	/**
	 * [使用指定的字符串生成秘钥
	 */
	public static String getAesByKey(String key) {
		if(StringUtil.isEmpty(key)) {
			return key;
		}
		String resultString = null;
		try {
			int sixteen = 16;
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(128, new SecureRandom(key.getBytes()));
			SecretKey sk = kg.generateKey();
			byte[] b = sk.getEncoded();
			resultString = byteToHexString(b);
			if(StringUtil.isNotEmpty(resultString) && resultString.length() > sixteen) {
				resultString = resultString.substring(sixteen);
			}
		} catch (NoSuchAlgorithmException e) {
			log.error("没有此算法",e);
		}
		return resultString;
	}
	
	/**
	 * byte数组转化为16进制字符串
	 * @param bytes
	 * @return
	 */
	public static String byteToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte aByte : bytes) {
			String strHex = Integer.toHexString(aByte);
			if (strHex.length() > 3) {
				sb.append(strHex.substring(6));
			} else {
				if (strHex.length() < 2) {
					sb.append("0").append(strHex);
				} else {
					sb.append(strHex);
				}
			}
		}
	 return sb.toString();
   }
		
}