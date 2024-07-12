package com.damai.util;


import cn.hutool.crypto.KeyUtil;
import com.alibaba.fastjson.JSON;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: rsa工具
 * @author: 阿星不是程序员
 **/
@Slf4j
public class RsaTool {
	
	private final static Integer KEY_SIZE = 2048;
	
	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 256;
	
	public static final String KEY_ALGORITHM = "RSA";
    
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";
	
	/**
	 * 生成公私钥
	 */
	public static Map<String, String> getKey() {
		Map<String, String> pubPriKey = new HashMap<>(8);
		KeyPair keyPair = KeyUtil.generateKeyPair(KEY_ALGORITHM, KEY_SIZE);
		String publicKeyStr = java.util.Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
		String privateKeyStr = java.util.Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
		pubPriKey.put(PUBLIC_KEY, publicKeyStr);
		pubPriKey.put(PRIVATE_KEY, privateKeyStr);
		return pubPriKey;
	}
	
	
	/**
	 * RSA加密
	 *
	 * @param data
	 *            待加密数据
	 * @param publicKey
	 *            公钥
	 * @return
	 */
	public static String encrypt(String data, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		int inputLen = data.getBytes().length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offset = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offset > 0) {
			if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
			}
			out.write(cache, 0, cache.length);
			i++;
			offset = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		// 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
		// 加密后的字符串
		return new String(Base64.encodeBase64(encryptedData));
	}
	
	/**
	 * RSA解密
	 *
	 * @param data
	 *            待解密数据
	 * @param privateKey
	 *            私钥
	 * @return
	 */
	public static String decrypt(String data, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] dataBytes = Base64.decodeBase64(data.replaceAll("%2B","+").getBytes());
		int inputLen = dataBytes.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offset = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offset > 0) {
			if (inputLen - offset > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
			}
			out.write(cache, 0, cache.length);
			i++;
			offset = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		// 解密后的内容
		return new String(decryptedData, "UTF-8");
	}
	
	/**
	 * 37 * 获取私钥 38 * 39 * @param privateKey 私钥字符串 40 * @return 41
	 */
	public static PrivateKey getPrivateKey(String privateKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
		return keyFactory.generatePrivate(keySpec);
	}
	
	/**
	 * 根据私钥字符串进行解密
	 * @param data
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data, String privateKeyStr) {
		try {
			PrivateKey privateKey2 = RsaTool.getPrivateKey(privateKeyStr);
			return RsaTool.decrypt(data, privateKey2);
		}catch (Exception e) {
			log.error("decrypt error",e);
			throw new DaMaiFrameException(BaseCode.RSA_DECRYPT_ERROR);
		}
	}
	
	/**
	 * 根据公钥串对数据进行RSA加密
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String publicKey)  {
		try {
			PublicKey publicKeyTmp = RsaTool.getPublicKey(publicKey);
			return encrypt(data,publicKeyTmp);
		}catch (Exception e) {
			log.error("encrypt error",e);
			throw new DaMaiFrameException(BaseCode.RSA_ENCRYPT_ERROR);
		}
			
	}
		
	/**
	 * 获取公钥
	 *
	 * @param publicKey
	 *            公钥字符串
	 * @return
	 */
	public static PublicKey getPublicKey(String publicKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
		return keyFactory.generatePublic(keySpec);
	}
	
	public static void main(String[] args) throws Exception {
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAirLDI4SPxLXAjk+CMJWrdREnQjJJQgEd7RAw+ZCPZKBFfkoPa5YjcYQzqtc4RPOszBZhPmGr732WLA0O2U0WFnPG6vva9x7pYQot4u5IoncRl7kBb89d1XdR5DZxKovQyDM91CkLikq8h0sBVTkfX2Jz34LmYd8TPQ4BSHUDE5h+f42WkUYG9PCaXvPg+yv4+1AwJeXI/wW181h1JQ5cmogFXIHEFOxS/wwtnoijwmRv/3nKhdyYZbpC2V7F2xq9jWuTBL01Oj3sRhbykHDW2aK2oJ53U5vqlaC6XsheCabMqeqjDPCa8rUjp10pWy7LneYxVigVuONOmlvt56ja7QIDAQAB";
		String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCKssMjhI/EtcCOT4Iwlat1ESdCMklCAR3tEDD5kI9koEV+Sg9rliNxhDOq1zhE86zMFmE+YavvfZYsDQ7ZTRYWc8bq+9r3HulhCi3i7kiidxGXuQFvz13Vd1HkNnEqi9DIMz3UKQuKSryHSwFVOR9fYnPfguZh3xM9DgFIdQMTmH5/jZaRRgb08Jpe8+D7K/j7UDAl5cj/BbXzWHUlDlyaiAVcgcQU7FL/DC2eiKPCZG//ecqF3JhlukLZXsXbGr2Na5MEvTU6PexGFvKQcNbZoragnndTm+qVoLpeyF4Jpsyp6qMM8JrytSOnXSlbLsud5jFWKBW4406aW+3nqNrtAgMBAAECggEAbCHOTSSOSZhBlTGbmHE3iT9kUhGOV60zPZ0/8XGouZTSWRE4UHJvE5M0DN9Z+TfY4gwYqF/RghdxOsq7ZuLYc4yz6oOMRNmOrZ8YAzIu4qrdxmHwItGSoFg0Oi3PsJHspgh9DakqXBjEPt5VHbI5KU5CdGFDZ85Y22LN0UWYrm8wOj6P4qJU/bXIOYYl4LfQRIdF6z/0a/ooi0JvQWfgiVMjymTaeF/aRNxqt2Mm09hWEfKUzYX96LINrCJ0DG/Rz+xyShW3rajOHxdY61gyIPybQcXehtacGW+DE/4M6KrWZYUoH/X3KUaXjq0Ed+Sj2cSmv30idcdyDsRmSK7VaQKBgQDOTVoMVhrzDAZf1DywMwSyObQJ4tFr1MwNLXD1wqRJta07448IuRGOdXoq4hoEw1gBAcHyWlIT/8hY7fiBvMi0YETL2gN+PWLhJL24dxR92ACsKP1j3cYM6b0HmfyrbTUuzk+dWcI835ADzWq+b093+EUI/7VZUAALowk2o/DtQwKBgQCsHEgxK7cW6nRLevssnnBIWQ5IlJlUsp/tqyn+IKwuUyJzJWRXvxag4UdMUcMOB/syw9XFyqBgEc+cY1jgoMVsacFyeAkUjfaxGro83H/Inx2Ge25zH/OxZkmL7kkz+bApKhmtHva8mCGu4ZHh0B6PFoffZV6idiAokgVmir/8DwKBgQCrDv5cfkUIRG9ApFXR7+uz8B61l8n39FFhl80zKjpZF/hVUUF3hSTmj8hFqIbUbjkZVKDBWFz4Uj2IZ4GH6cYtsik5MkN1OGc1seZR/wMRuboNBkvcs7YVXPYtSGR2rC3N6qmfGh7xpJngXUJmNxuYqVZsuMJhFPGEtKHeGZ+aywKBgHEPsyz59rCLHBJpm47YFhKwzf1IAOHu5biPdGqItBNKcZsKuTwbP5Y350pve59ABvh2RXxFe80gZi3p5XzKoGZzoqy7xdtG1wPI9wb8IsV8IT0y4H+oQcIL28ycoGIQaHTiPzPG33dMyPPFIrwgp7J/ropGYUCAMOf15K5T/4JpAoGAHwLE5jaJxK5VKKe9x4uPWcPLMgJY3s9J2dn1ZrZTOKqE0d9GCbSEhZNtGOrAzAmWdy3GC0rmP0Fs2DIyTJg9iPsn/ISt0PYvIzSJ6CQeAuEtsjdEdKiVa/um10XeD4dT4vAyMHJpg9WV2NR/vjiuk2YIM1CXo/r/7Gp6aiHY+Bc=";
		Map<String,Object> businessBodyMap = new HashMap<>(8);
		businessBodyMap.put("id","1");
		businessBodyMap.put("sleepTime","10");
		String encrypt = RsaTool.encrypt(JSON.toJSONString(businessBodyMap), publicKey);
		System.out.println("加密后:" + encrypt);
		String decrypt = RsaTool.decrypt(encrypt, privateKey);
		System.out.println("解密后:" + decrypt);
	}

}
