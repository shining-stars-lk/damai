package com.example.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author lk
 *
 */
public class SignatureUtil {
	
	/**
	 * RSA/RSA2 签名
	 * @param content
	 * @param privateKey
	 * @param charset
	 * @param signType
	 * @return
	 */
	public static String rsaSign(String content, String privateKey, String charset, String signType) {
		return RSA.rsaSign(content, privateKey, charset, signType);
	}
	
	/**
	 * RSA/RSA2 签名
	 * @param params
	 * @param privateKey
	 * @param charset
	 * @param signType
	 * @return
	 */
	public static String rsaSign(Map<String, String> params, String privateKey, String charset,String signType) {
		String content = buildParam(params);
		return RSA.rsaSign(content, privateKey, charset, signType);
	}
	
	/**
	 * RSA 签名
	 * @param content
	 * @param privateKey
	 * @param charset
	 * @return
	 */
	public static String rsaSign(String content, String privateKey, String charset) {
		return RSA.rsaSign(content, privateKey, charset);
	}
	
	/**
	 * RSA 签名
	 * @param params
	 * @param privateKey
	 * @param charset
	 * @return
	 */
	public static String rsaSign(Map<String, String> params, String privateKey, String charset) {
		String content = buildParam(params);
		return RSA.rsaSign(content, privateKey, charset);
	}
	
	/**
	 * RSA2 签名
	 * @param content
	 * @param privateKey
	 * @param charset
	 * @return
	 */
	public static String rsa256Sign(String content, String privateKey, String charset) {
		return RSA.rsa256Sign(content, privateKey, charset);
	}
	
	/**
	 * RSA2 签名
	 * @param params
	 * @param privateKey
	 * @param charset
	 * @return
	 */
	public static String rsa256Sign(Map<String, String> params, String privateKey, String charset) {
		String content = buildParam(params);
		return RSA.rsa256Sign(content, privateKey, charset);
	}
	
	/**
	 * RSA/RSA2 验签
	 * @param content
	 * @param sign
	 * @param publicKey
	 * @param charset
	 * @param signType
	 * @return
	 */
	public static boolean rsaCheck(String content, String sign, String publicKey, String charset, String signType) {
		return RSA.rsaCheck(content, sign, publicKey, charset, signType);
	}
	
	/**
	 * RSA/RSA2 验签
	 * @param params
	 * @param publicKey
	 * @param charset
	 * @param signType
	 * @return
	 */
	public static boolean rsaCheck(Map<String, String> params, String publicKey, String charset,String signType) {
		String sign = params.get("sign");
		String content = getSignCheckContent(params);
		
		return rsaCheck(content, sign, publicKey, charset,signType);
	}
	
	/**
	 * RSA 验签
	 * @param content
	 * @param sign
	 * @param publicKey
	 * @param charset
	 * @return
	 */
	public static boolean rsaCheck(String content, String sign, String publicKey, String charset) {
		return RSA.rsaCheck(content, sign, publicKey, charset);
	}
	
	/**
	 * RSA 验签
	 * @param params
	 * @param publicKey
	 * @param charset
	 * @return
	 */
	public static boolean rsaCheck(Map<String, String> params, String publicKey, String charset) {
		String sign = params.get("sign");
		String content = getSignCheckContent(params);
		
		return RSA.rsaCheck(content, sign, publicKey, charset);
	}
	
	/**
	 * RSA2 验签
	 * @param content
	 * @param sign
	 * @param publicKey
	 * @param charset
	 * @return
	 */
	public static boolean rsa256Check(String content, String sign, String publicKey, String charset) {
		return RSA.rsa256Check(content, sign, publicKey, charset);
	}
	
	/**
	 * RSA2 验签
	 * @param params
	 * @param publicKey
	 * @param charset
	 * @return
	 */
	public static boolean rsa256Check(Map<String, String> params, String publicKey, String charset) {
		String sign = params.get("sign");
		String content = getSignCheckContent(params);
		
		return RSA.rsa256Check(content, sign, publicKey, charset);
	}
	
	/**
	 * 获取签名检查的内容
	 * @param params
	 * @return
	 */
	private static String getSignCheckContent(Map<String, String> params) {
		if (params == null) {
            return null;
        }

        params.remove("sign");
        params.remove("files");
        
        return buildParam(params);
	}
	
	/**
	 * 构建参数字符串
	 * @param params
	 * @return
	 */
	private static String buildParam(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = params.get(key);
			sb.append(buildKeyValue(key, value, false));
			sb.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = params.get(tailKey);
		sb.append(buildKeyValue(tailKey, tailValue, false));

		return sb.toString();
	}
	
	/**
	 * 国大药企签名方法
	 * @param params
	 * @param aesKey
	 * @return
	 */
	public static String md5sign(Map<String, String> params, String aesKey) {
		String content = buildParam(params);
		content = content+"&key="+aesKey;
		return MD5Generator.MD5(content).toUpperCase();
	}
	
	/**
	 * 拼接键值对
	 * 
	 * @param key
	 * @param value
	 * @param isEncode
	 * @return
	 */
	private static String buildKeyValue(String key, String value, boolean isEncode) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append("=");
		if (isEncode) {
			try {
				sb.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				sb.append(value);
			}
		} else {
			sb.append(value);
		}
		return sb.toString();
	}
	
	/**
	 * 华润药企入参签名函数
	 * @param params
	 * @param aesKey
	 * @return
	 */
	public static String md5signObj(Map<String, Object> params, String aesKey) {
		String content = buildParamObj(params);
		content = content+"&key="+aesKey;
		return MD5Generator.MD5(content);
	}
	
	private static String buildParamObj(Map<String, Object> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			Object value = params.get(key);
			sb.append(buildKeyValueObj(key, value));
			sb.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		Object tailValue = params.get(tailKey);
		sb.append(buildKeyValueObj(tailKey, tailValue));

		return sb.toString();
	}
	
	/**
	 * 拼接键值对
	 * 
	 * @param key
	 * @param value
	 * @param isEncode
	 * @return
	 */
	private static String buildKeyValueObj(String key, Object value) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append("=");
		sb.append(value);
		return sb.toString();
	}
	
}
