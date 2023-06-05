package com.example.util;

import com.example.enums.BaseCode;
import com.example.exception.ToolkitException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * DES加密算法
 * @author zhangwj
 * @date 2016年1月25日
 * @version 0.0.1
 */
@Slf4j
public class DesAlgorithm {
	
	private final static String DES = "DES";
	
	/**
	 * 根据键值进行加密
	 * @param data    要加密的内容
	 * @return String 返回类型 	加密后字符串
	 * @throws Exception
	 */
	public static String encrypt(String data) throws Exception {
		String key = RandomStringGenerator.gen8DigitalString();
		byte[] bt = encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
//		String strs =  Encodes.encodeBase62(bt);
//		String strs = new BASE64Encoder().encode(bt);
		String strs =  Base64.encode(bt);
		StringBuffer sbBuffer = new StringBuffer(key);
		strs = sbBuffer.reverse().toString() + strs;
		return strs;
	}
	
	/**
	 * 根据键值进行加密
	 * @param data    要加密的内容
	 * @return String 返回类型 	加密后字符串
	 * @throws Exception
	 */
	public static String encryptBase62(String data) throws Exception {
		String key = RandomStringGenerator.gen8DigitalString();
		byte[] bt = encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
		String strs =  Encodes.encodeBase62(bt);
//		String strs = new BASE64Encoder().encode(bt);
//		String strs =  Base64.encode(bt);
		StringBuffer sbBuffer = new StringBuffer(key);
		strs = sbBuffer.reverse().toString() + strs;
		return strs;
	}
	
	/**
	 * 根据键值进行加密
	 * @param data    要加密的内容
	 * @param key    加密键
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) {
		try {
			byte[] bt = encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
			String strs =  Base64.encode(bt);
			return strs;
		}catch (Exception e) {
			throw new ToolkitException(BaseCode.AES_ERROR);
		}
		
	}
		
		/**
		 * 根据键值进行解密
		 * @param data     要解密的内容
		 * @return String 返回类型 	解密后字符串
		 * @throws IOException
		 * @throws Exception
		 */
		public static String decrypt(String data) throws IOException, Exception {
			if (data == null)
				return null;
			StringBuffer sbBuffer = new StringBuffer(data.substring(0, 8));
			String key = sbBuffer.reverse().toString();
			data = data.substring(8);
			
			
			//		BASE64Decoder decoder = new BASE64Decoder();
//		byte[] buf = decoder.decodeBuffer(data);
//		byte[] buf = Encodes.decodeBase62(data.toCharArray());
			byte[] buf = Base64.decode(data);
			if(buf == null) {
				return null;
			}
			byte[] bt = decrypt(buf, key.getBytes("UTF-8"));
			
			
			return new String(bt, "UTF-8");
		}
		
		//decryptNew移睿医生专用
		public static String decryptNew(String data) throws IOException, Exception {
			if (data == null)
				return null;
			StringBuffer sbBuffer = new StringBuffer(data.substring(0, 8));
			String key = sbBuffer.reverse().toString();
			data = data.substring(8);
			
			
			//		BASE64Decoder decoder = new BASE64Decoder();
//		byte[] buf = decoder.decodeBuffer(data);
			byte[] buf = Encodes.decodeBase62(data.toCharArray());
//		byte[] buf = Base64.decode(data);
			byte[] bt = decrypt(buf, key.getBytes("UTF-8"));
			
			
			return new String(bt, "UTF-8");
		}
		
		/**
		 * 根据键值进行解密
		 * @param data     要解密的内容
		 * @return String 返回类型 	解密后字符串
		 * @throws IOException
		 * @throws Exception
		 */
		public static String decryptBase62(String data) throws IOException, Exception {
			if (data == null)
				return null;
			StringBuffer sbBuffer = new StringBuffer(data.substring(0, 8));
			String key = sbBuffer.reverse().toString();
			data = data.substring(8);
			
			
			//		BASE64Decoder decoder = new BASE64Decoder();
//		byte[] buf = decoder.decodeBuffer(data);
			byte[] buf = Encodes.decodeBase62(data.toCharArray());
//		byte[] buf = Base64.decode(data);
			byte[] bt = decrypt(buf, key.getBytes("UTF-8"));
			
			
			return new String(bt, "UTF-8");
		}
		
		/**
		 * 根据键值进行解密
		 * @param data    要解密的内容
		 * @param key    加密键
		 * @return
		 * @throws IOException
		 * @throws Exception
		 */
		public static String decrypt(String data, String key) {
			try {
				if (data == null)
					return null;
				byte[] buf = Base64.decode(data);
				byte[] bt = decrypt(buf, key.getBytes("UTF-8"));
				return new String(bt, "UTF-8");
			}catch (Exception e) {
				log.error("decrypt error data : {}, key :{}",data, key, e);
				throw new ToolkitException(BaseCode.AES_ERROR);
			}
		}
		
		/**
		 * 根据键值进行加密
		 * @param data    要加密的内容的byte数组
		 * @param key    加密键byte数组
		 * @return
		 * @throws Exception
		 */
		private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
			// 生成一个可信任的随机数源
			SecureRandom sr = new SecureRandom();

//		生成秘钥
			SecretKey securekey = getSecretKey(key);
			
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(DES);
			
			// 用密钥初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
			
			return cipher.doFinal(data);
		}
		
		/**
		 * 根据键值进行解密
		 * @param data    要解密的内容的byte数组
		 * @param key    加密键byte数组
		 * @return
		 * @throws Exception
		 */
		private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
			// 生成一个可信任的随机数源
			SecureRandom sr = new SecureRandom();

//		生成秘钥
			SecretKey securekey = getSecretKey(key);
			
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance(DES);
			
			// 用密钥初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
			
			return cipher.doFinal(data);
		}
		
		/**
		 * 生成秘钥
		 * @param key
		 * @return
		 * @throws InvalidKeyException
		 * @throws NoSuchAlgorithmException
		 * @throws InvalidKeySpecException
		 */
		private static SecretKey getSecretKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);
			
			// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey securekey = keyFactory.generateSecret(dks);
			return securekey;
		}
		
		public static void main(String[] args) throws Exception {
			String content = "docId=69582&doctorUserId=085db53a90b111e89a00005056ac3223&hospitalNo=40068980X4&app=40068980X4_1";
			String encryptContent = DesAlgorithm.encrypt(content);
			System.out.println("加密后内容为：" + encryptContent);
			String decryptContent = DesAlgorithm.decrypt(encryptContent);
			System.out.println("解密后内容为：" + decryptContent);
			
			
			String key = "12345678";
			String encryptContent2 = DesAlgorithm.encrypt(content,key);
			System.out.println("2加密后内容为：" + encryptContent2);
			String decryptContent2 = DesAlgorithm.decrypt(encryptContent2, key);
			System.out.println("2解密后内容为：" + decryptContent2);
			
		}
		
		/**
		 * SHA1加密
		 * @param value
		 * @return
		 */
		public static String hexSHA1(String value) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(value.getBytes("utf-8"));
				byte[] digest = md.digest();
				return byteToHexString(digest);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		
		
		public static String byteToHexString(byte[] bytes) {
			return String.valueOf(Hex.encodeHex(bytes));
		}
}