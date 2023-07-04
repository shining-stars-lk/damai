package com.example.util;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SignUtil
 * @Description:
 * @author lk
 * @date 2023-5-18
 */
public class SignUtil {
	
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
	 * @return
	 */
	private static String buildKeyValueObj(String key, Object value) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append("=");
		sb.append(value);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("code","1324");
		Map<String,Object> businessBodyMap = new HashMap<>();
		businessBodyMap.put("id","1");
		businessBodyMap.put("sleepTime","10");
		map.put("businessBody", JSON.toJSONString(businessBodyMap));
		
		String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCht3FKchtYAS4Gcjb2bEOfAXA+vcVSZjTV3JaS7sqOSFbDGDV2o1nLuPktdr5ByjUtA+SOBJuzEKm3qWueVq8jGAl9FiLie0TiWnHrre95zPQRe1PE8LvqQnk81mFAYv3KZWyTFb6Ky4O6CWnr2zGI1QM5JNA+Jm/AhwDxWXjN1yVzNhWi3fdBPl3WajI/hI+WWwqs9Vm3GI0e3IfN1cDxyecjoWbJKIZ1quzRm+FcifcjQaDF72+QHtnik1BaNKzbmvVmJGypvi9X450wPnUJNvhOl/t0CUfbdSeihseo2WOf+Z9p5NQUuRdKFgISRT6yxd9746liqXkpxm4NMIahAgMBAAECggEAFtoiUz/Op1/7TgPjymzAHX8JioQslxlETBhQ2tCNpQ+J2yXXoD0zGju4UnleJ1PYsdTD/mGeUu5+3So+v/BF7XKfHKL9KP38XPQk9wXsOk0BDFteGg1esJrWIQe2VG/opyov7pT7CQf7RFXCNwcRd+GKBBA0sSOjVRR+yJw5GvUbm6Ytf/EkCwLNXrsEFhlcXyE5N29TARrHau9JA1DkjX10O1f3Q80p4q4IaNXm7NIWJ8M4TAhN8KTYyTNZruXD5vHlv/NPTdCaCIAS863Mg46qkXXJ3wrjsCKy48GWAKWQiiMHf9p7F9cUPLQZdjuyxmfrJwWeiLzoFo63tDbpcQKBgQDQHtcKnYIB6vTkf8FOXl2wwd0INkZjr2YVJG8MJzB4HdRwuyYYHGbzR4Xk9ag+kg5En3nykA+cMxn+54eQmQo1aMX8En/bla6UJ8AvBVLP068JzR9KdQ49AQqq/74aLEFr0lL8ectXRCJz1pohZ8Gs7DtuMio/4AXJqL9gisolVQKBgQDG66qPH2GK6Hdn4ZTv5qPw7CpogKzZGpVKg4Q8Bztxnj+p22MoiMDdZ89O0mgEytecPSRro7ziyhp1zoJzULru9NdxTPqx1OZW9nj46elYfZAqbo3NJByNjDQrBZDtxwUfz4rkpegliZ/ljRJjWh/Oa8UyGSJwfnsVRaDnx2EcHQKBgFFKGnhc+TDCkxDFDb4Mgc/OiQTyHiBFnDvZ1T4L+JSSIi4+Cy0Tuup/Hz9E7Ig0CDqph7pEprQ+CYNU79B81k3yNJK2rxYXqu7Xb+ttyuC+L/pGEljEy+DsDTypU5lpe8wfhKZ09AWL6WERi3ZMzos6YiQyl+oHGHuh285bp4VZAoGAF3WZotFvnoM1+dFX0EciFHq1sadjOyNwcd46zR2JPCgOmAiglBo0rKfeggw8ajxF2042ql8gGpr9LeGR7umZci777YfHlQtnst/Uen6Tn3UHeImbPZNBrsvXJy+73N740ryWQ8rxKuQlMFxHy+HIGH8LPZJLRnsUJvkUNeGEqV0CgYAr9K/w9ruTVKs5/Q2dGMhZLVadsKMo6b4rMzuO5/iH94q2XgzHKw+to4zUJUyCiwxW9w2rM4xSZu20zfBl4Lt4VNllHbLAWU0onPa24ZBiHv9IZKBtzPpyYdi6Y5/D6DfNNTQO6B/XvyzCikhtBFlZSVj23rLqhFD9N/gWP/kMaA==";
		String sign = SignUtil.rsa256Sign(map, privateKey, "utf-8");
		System.out.println("签名:" + sign);
		
		map.put("sign",sign);
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAobdxSnIbWAEuBnI29mxDnwFwPr3FUmY01dyWku7KjkhWwxg1dqNZy7j5LXa+Qco1LQPkjgSbsxCpt6lrnlavIxgJfRYi4ntE4lpx663vecz0EXtTxPC76kJ5PNZhQGL9ymVskxW+isuDuglp69sxiNUDOSTQPiZvwIcA8Vl4zdclczYVot33QT5d1moyP4SPllsKrPVZtxiNHtyHzdXA8cnnI6FmySiGdars0ZvhXIn3I0Ggxe9vkB7Z4pNQWjSs25r1ZiRsqb4vV+OdMD51CTb4Tpf7dAlH23UnoobHqNljn/mfaeTUFLkXShYCEkU+ssXfe+OpYql5KcZuDTCGoQIDAQAB";
		boolean b = SignUtil.rsa256Check(map, publicKey, "utf-8");
		System.out.println("签名结果:" + b);
	}
	
}
