//package com.example.util;
//
//import com.alibaba.fastjson.JSON;
//import com.example.enums.BaseCode;
//import com.example.exception.CookFrameException;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @ClassName: SignUtil
// * @Description:
// * @author 星哥
// * @date 2023-5-18
// */
//@Slf4j
//public class SignUtil {
//	
//	/**
//	 * RSA/RSA2 签名
//	 * @param content
//	 * @param privateKey
//	 * @param charset
//	 * @param signType
//	 * @return
//	 */
//	public static String rsaSign(String content, String privateKey, String charset, String signType) {
//		return RSA.rsaSign(content, privateKey, charset, signType);
//	}
//	
//	/**
//	 * RSA/RSA2 签名
//	 * @param params
//	 * @param privateKey
//	 * @param charset
//	 * @param signType
//	 * @return
//	 */
//	public static String rsaSign(Map<String, String> params, String privateKey, String charset,String signType) {
//		String content = buildParam(params);
//		return RSA.rsaSign(content, privateKey, charset, signType);
//	}
//	
//	/**
//	 * RSA 签名
//	 * @param content
//	 * @param privateKey
//	 * @param charset
//	 * @return
//	 */
//	public static String rsaSign(String content, String privateKey, String charset) {
//		return RSA.rsaSign(content, privateKey, charset);
//	}
//	
//	/**
//	 * RSA 签名
//	 * @param params
//	 * @param privateKey
//	 * @param charset
//	 * @return
//	 */
//	public static String rsaSign(Map<String, String> params, String privateKey, String charset) {
//		String content = buildParam(params);
//		return RSA.rsaSign(content, privateKey, charset);
//	}
//	
//	/**
//	 * RSA2 签名
//	 * @param content
//	 * @param privateKey
//	 * @param charset
//	 * @return
//	 */
//	public static String rsa256Sign(String content, String privateKey, String charset) {
//		return RSA.rsa256Sign(content, privateKey, charset);
//	}
//	
//	/**
//	 * RSA2 签名
//	 * @param params
//	 * @param privateKey
//	 * @param charset
//	 * @return
//	 */
//	public static String rsa256Sign(Map<String, String> params, String privateKey, String charset) {
//		String content = buildParam(params);
//		return RSA.rsa256Sign(content, privateKey, charset);
//	}
//	
//	/**
//	 * RSA/RSA2 验签
//	 * @param content
//	 * @param sign
//	 * @param publicKey
//	 * @param charset
//	 * @param signType
//	 * @return
//	 */
//	public static boolean rsaCheck(String content, String sign, String publicKey, String charset, String signType) {
//		return RSA.rsaCheck(content, sign, publicKey, charset, signType);
//	}
//	
//	/**
//	 * RSA/RSA2 验签
//	 * @param params
//	 * @param publicKey
//	 * @param charset
//	 * @param signType
//	 * @return
//	 */
//	public static boolean rsaCheck(Map<String, String> params, String publicKey, String charset,String signType) {
//		String sign = params.get("sign");
//		String content = getSignCheckContent(params);
//		
//		return rsaCheck(content, sign, publicKey, charset,signType);
//	}
//	
//	/**
//	 * RSA 验签
//	 * @param content
//	 * @param sign
//	 * @param publicKey
//	 * @param charset
//	 * @return
//	 */
//	public static boolean rsaCheck(String content, String sign, String publicKey, String charset) {
//		return RSA.rsaCheck(content, sign, publicKey, charset);
//	}
//	
//	/**
//	 * RSA 验签
//	 * @param params
//	 * @param publicKey
//	 * @param charset
//	 * @return
//	 */
//	public static boolean rsaCheck(Map<String, String> params, String publicKey, String charset) {
//		String sign = params.get("sign");
//		String content = getSignCheckContent(params);
//		
//		return RSA.rsaCheck(content, sign, publicKey, charset);
//	}
//	
//	/**
//	 * RSA2 验签
//	 * @param content
//	 * @param sign
//	 * @param publicKey
//	 * @param charset
//	 * @return
//	 */
//	public static boolean rsa256Check(String content, String sign, String publicKey, String charset) {
//		return RSA.rsa256Check(content, sign, publicKey, charset);
//	}
//	
//	/**
//	 * RSA2 验签
//	 * @param params
//	 * @param publicKey
//	 * @param charset
//	 * @return
//	 */
//	public static boolean rsa256Check(Map<String, String> params, String publicKey, String charset) {
//		try {
//			String sign = params.get("sign");
//			String content = getSignCheckContent(params);
//			
//			return RSA.rsa256Check(content, sign, publicKey, charset);
//		}catch (Exception e) {
//			log.error("rsa256Check error",e);
//			throw new CookFrameException(BaseCode.RSA_SIGN_ERROR);
//		}
//			
//	}
//		
//	/**
//	 * 获取签名检查的内容
//	 * @param params
//	 * @return
//	 */
//	private static String getSignCheckContent(Map<String, String> params) {
//		if (params == null) {
//		return null;
//	}
//
//	params.remove("sign");
//	params.remove("files");
//	
//	return buildParam(params);
//	}
//	
//	/**
//	 * 构建参数字符串
//	 * @param params
//	 * @return
//	 */
//	private static String buildParam(Map<String, String> params) {
//		List<String> keys = new ArrayList<String>(params.keySet());
//		Collections.sort(keys);
//		
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < keys.size() - 1; i++) {
//			String key = keys.get(i);
//			String value = params.get(key);
//			sb.append(buildKeyValue(key, value, false));
//			sb.append("&");
//		}
//		
//		String tailKey = keys.get(keys.size() - 1);
//		String tailValue = params.get(tailKey);
//		sb.append(buildKeyValue(tailKey, tailValue, false));
//		
//		return sb.toString();
//	}
//	
//	/**
//	 * 拼接键值对
//	 *
//	 * @param key
//	 * @param value
//	 * @param isEncode
//	 * @return
//	 */
//	private static String buildKeyValue(String key, String value, boolean isEncode) {
//		StringBuilder sb = new StringBuilder();
//		sb.append(key);
//		sb.append("=");
//		if (isEncode) {
//			try {
//				sb.append(URLEncoder.encode(value, "UTF-8"));
//			} catch (UnsupportedEncodingException e) {
//				sb.append(value);
//			}
//		} else {
//			sb.append(value);
//		}
//		return sb.toString();
//	}
//	
//	public static void main(String[] args) {
//		String signPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCht3FKchtYAS4Gcjb2bEOfAXA+vcVSZjTV3JaS7sqOSFbDGDV2o1nLuPktdr5ByjUtA+SOBJuzEKm3qWueVq8jGAl9FiLie0TiWnHrre95zPQRe1PE8LvqQnk81mFAYv3KZWyTFb6Ky4O6CWnr2zGI1QM5JNA+Jm/AhwDxWXjN1yVzNhWi3fdBPl3WajI/hI+WWwqs9Vm3GI0e3IfN1cDxyecjoWbJKIZ1quzRm+FcifcjQaDF72+QHtnik1BaNKzbmvVmJGypvi9X450wPnUJNvhOl/t0CUfbdSeihseo2WOf+Z9p5NQUuRdKFgISRT6yxd9746liqXkpxm4NMIahAgMBAAECggEAFtoiUz/Op1/7TgPjymzAHX8JioQslxlETBhQ2tCNpQ+J2yXXoD0zGju4UnleJ1PYsdTD/mGeUu5+3So+v/BF7XKfHKL9KP38XPQk9wXsOk0BDFteGg1esJrWIQe2VG/opyov7pT7CQf7RFXCNwcRd+GKBBA0sSOjVRR+yJw5GvUbm6Ytf/EkCwLNXrsEFhlcXyE5N29TARrHau9JA1DkjX10O1f3Q80p4q4IaNXm7NIWJ8M4TAhN8KTYyTNZruXD5vHlv/NPTdCaCIAS863Mg46qkXXJ3wrjsCKy48GWAKWQiiMHf9p7F9cUPLQZdjuyxmfrJwWeiLzoFo63tDbpcQKBgQDQHtcKnYIB6vTkf8FOXl2wwd0INkZjr2YVJG8MJzB4HdRwuyYYHGbzR4Xk9ag+kg5En3nykA+cMxn+54eQmQo1aMX8En/bla6UJ8AvBVLP068JzR9KdQ49AQqq/74aLEFr0lL8ectXRCJz1pohZ8Gs7DtuMio/4AXJqL9gisolVQKBgQDG66qPH2GK6Hdn4ZTv5qPw7CpogKzZGpVKg4Q8Bztxnj+p22MoiMDdZ89O0mgEytecPSRro7ziyhp1zoJzULru9NdxTPqx1OZW9nj46elYfZAqbo3NJByNjDQrBZDtxwUfz4rkpegliZ/ljRJjWh/Oa8UyGSJwfnsVRaDnx2EcHQKBgFFKGnhc+TDCkxDFDb4Mgc/OiQTyHiBFnDvZ1T4L+JSSIi4+Cy0Tuup/Hz9E7Ig0CDqph7pEprQ+CYNU79B81k3yNJK2rxYXqu7Xb+ttyuC+L/pGEljEy+DsDTypU5lpe8wfhKZ09AWL6WERi3ZMzos6YiQyl+oHGHuh285bp4VZAoGAF3WZotFvnoM1+dFX0EciFHq1sadjOyNwcd46zR2JPCgOmAiglBo0rKfeggw8ajxF2042ql8gGpr9LeGR7umZci777YfHlQtnst/Uen6Tn3UHeImbPZNBrsvXJy+73N740ryWQ8rxKuQlMFxHy+HIGH8LPZJLRnsUJvkUNeGEqV0CgYAr9K/w9ruTVKs5/Q2dGMhZLVadsKMo6b4rMzuO5/iH94q2XgzHKw+to4zUJUyCiwxW9w2rM4xSZu20zfBl4Lt4VNllHbLAWU0onPa24ZBiHv9IZKBtzPpyYdi6Y5/D6DfNNTQO6B/XvyzCikhtBFlZSVj23rLqhFD9N/gWP/kMaA==";
//		String signPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAobdxSnIbWAEuBnI29mxDnwFwPr3FUmY01dyWku7KjkhWwxg1dqNZy7j5LXa+Qco1LQPkjgSbsxCpt6lrnlavIxgJfRYi4ntE4lpx663vecz0EXtTxPC76kJ5PNZhQGL9ymVskxW+isuDuglp69sxiNUDOSTQPiZvwIcA8Vl4zdclczYVot33QT5d1moyP4SPllsKrPVZtxiNHtyHzdXA8cnnI6FmySiGdars0ZvhXIn3I0Ggxe9vkB7Z4pNQWjSs25r1ZiRsqb4vV+OdMD51CTb4Tpf7dAlH23UnoobHqNljn/mfaeTUFLkXShYCEkU+ssXfe+OpYql5KcZuDTCGoQIDAQAB";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("code","1234");
//		
//		
//		//单纯的签名
////		map.put("businessBody", "{\"id\":\"1111\",\"sleepTime\":10}");
////		String sign = SignUtil.rsa256Sign(map, signPrivateKey, "utf-8");
////		System.out.println("签名:" + sign);
////		map.put("sign",sign);
////		boolean b = SignUtil.rsa256Check(map, signPublicKey, "utf-8");
////		System.out.println("签名结果:" + b);
//		
//		
//		//参数加密后再签名
//		Map<String, Object> businessMap = new HashMap();
//		businessMap.put("id","1111");
//		businessMap.put("sleepTime",10);
//		
//		String dataPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAirLDI4SPxLXAjk+CMJWrdREnQjJJQgEd7RAw+ZCPZKBFfkoPa5YjcYQzqtc4RPOszBZhPmGr732WLA0O2U0WFnPG6vva9x7pYQot4u5IoncRl7kBb89d1XdR5DZxKovQyDM91CkLikq8h0sBVTkfX2Jz34LmYd8TPQ4BSHUDE5h+f42WkUYG9PCaXvPg+yv4+1AwJeXI/wW181h1JQ5cmogFXIHEFOxS/wwtnoijwmRv/3nKhdyYZbpC2V7F2xq9jWuTBL01Oj3sRhbykHDW2aK2oJ53U5vqlaC6XsheCabMqeqjDPCa8rUjp10pWy7LneYxVigVuONOmlvt56ja7QIDAQAB";
//		String dataPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCKssMjhI/EtcCOT4Iwlat1ESdCMklCAR3tEDD5kI9koEV+Sg9rliNxhDOq1zhE86zMFmE+YavvfZYsDQ7ZTRYWc8bq+9r3HulhCi3i7kiidxGXuQFvz13Vd1HkNnEqi9DIMz3UKQuKSryHSwFVOR9fYnPfguZh3xM9DgFIdQMTmH5/jZaRRgb08Jpe8+D7K/j7UDAl5cj/BbXzWHUlDlyaiAVcgcQU7FL/DC2eiKPCZG//ecqF3JhlukLZXsXbGr2Na5MEvTU6PexGFvKQcNbZoragnndTm+qVoLpeyF4Jpsyp6qMM8JrytSOnXSlbLsud5jFWKBW4406aW+3nqNrtAgMBAAECggEAbCHOTSSOSZhBlTGbmHE3iT9kUhGOV60zPZ0/8XGouZTSWRE4UHJvE5M0DN9Z+TfY4gwYqF/RghdxOsq7ZuLYc4yz6oOMRNmOrZ8YAzIu4qrdxmHwItGSoFg0Oi3PsJHspgh9DakqXBjEPt5VHbI5KU5CdGFDZ85Y22LN0UWYrm8wOj6P4qJU/bXIOYYl4LfQRIdF6z/0a/ooi0JvQWfgiVMjymTaeF/aRNxqt2Mm09hWEfKUzYX96LINrCJ0DG/Rz+xyShW3rajOHxdY61gyIPybQcXehtacGW+DE/4M6KrWZYUoH/X3KUaXjq0Ed+Sj2cSmv30idcdyDsRmSK7VaQKBgQDOTVoMVhrzDAZf1DywMwSyObQJ4tFr1MwNLXD1wqRJta07448IuRGOdXoq4hoEw1gBAcHyWlIT/8hY7fiBvMi0YETL2gN+PWLhJL24dxR92ACsKP1j3cYM6b0HmfyrbTUuzk+dWcI835ADzWq+b093+EUI/7VZUAALowk2o/DtQwKBgQCsHEgxK7cW6nRLevssnnBIWQ5IlJlUsp/tqyn+IKwuUyJzJWRXvxag4UdMUcMOB/syw9XFyqBgEc+cY1jgoMVsacFyeAkUjfaxGro83H/Inx2Ge25zH/OxZkmL7kkz+bApKhmtHva8mCGu4ZHh0B6PFoffZV6idiAokgVmir/8DwKBgQCrDv5cfkUIRG9ApFXR7+uz8B61l8n39FFhl80zKjpZF/hVUUF3hSTmj8hFqIbUbjkZVKDBWFz4Uj2IZ4GH6cYtsik5MkN1OGc1seZR/wMRuboNBkvcs7YVXPYtSGR2rC3N6qmfGh7xpJngXUJmNxuYqVZsuMJhFPGEtKHeGZ+aywKBgHEPsyz59rCLHBJpm47YFhKwzf1IAOHu5biPdGqItBNKcZsKuTwbP5Y350pve59ABvh2RXxFe80gZi3p5XzKoGZzoqy7xdtG1wPI9wb8IsV8IT0y4H+oQcIL28ycoGIQaHTiPzPG33dMyPPFIrwgp7J/ropGYUCAMOf15K5T/4JpAoGAHwLE5jaJxK5VKKe9x4uPWcPLMgJY3s9J2dn1ZrZTOKqE0d9GCbSEhZNtGOrAzAmWdy3GC0rmP0Fs2DIyTJg9iPsn/ISt0PYvIzSJ6CQeAuEtsjdEdKiVa/um10XeD4dT4vAyMHJpg9WV2NR/vjiuk2YIM1CXo/r/7Gp6aiHY+Bc=";
//		
//		String encrypt = RSATool.encrypt(JSON.toJSONString(businessMap), dataPublicKey);
//		System.out.println("参数加密后:" + encrypt);
//		String decrypt = RSATool.decrypt(encrypt, dataPrivateKey);
//		System.out.println("参数解密后:" + decrypt);
//		
//		map.put("businessBody", decrypt);
//		String sign = SignUtil.rsa256Sign(map, signPrivateKey, "utf-8");
//		System.out.println("签名:" + sign);
//		map.put("sign",sign);
//		boolean b = SignUtil.rsa256Check(map, signPublicKey, "utf-8");
//		System.out.println("签名结果:" + b);
//	}
//	
//}
