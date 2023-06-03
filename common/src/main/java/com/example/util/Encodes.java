/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.example.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/***
* Description： 封装各种格式的编码解码工具类.
*              自制的base62 编码
*              Commons-Lang的xml/html escape
*              JDK提供的URLEncoder
*
* History：
 */
public class Encodes {
	private final static Log logger = LogFactory.getLog(Encodes.class);
	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	private static char[] encodes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray(); 
	private static byte[] decodes = new byte[256];  
	
	static {  
	    for (int i = 0; i < encodes.length; i++) {  
	        decodes[encodes[i]] = (byte) i;  
	    }  
	} 
	

	public static StringBuffer encodeBase64(byte[] data) {  
	    StringBuffer sb = new StringBuffer(data.length * 2);  
	    int pos = 0, val = 0;  
	    for (int i = 0; i < data.length; i++) {  
	        val = (val << 8) | (data[i] & 0xFF);  
	        pos += 8;  
	        while (pos > 5) {  
	            sb.append(encodes[val >> (pos -= 6)]);  
	            val &= ((1 << pos) - 1);  
	        }  
	    }  
	    if (pos > 0) {  
	        sb.append(encodes[val << (6 - pos)]);  
	    }  
	    return sb;  
	}  
	  
	public static byte[] decodeBase64(char[] data) {  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);  
	    int pos = 0, val = 0;  
	    for (int i = 0; i < data.length; i++) {  
	        val = (val << 6) | decodes[data[i]];  
	        pos += 6;  
	        while (pos > 7) {  
	            baos.write(val >> (pos -= 8));  
	            val &= ((1 << pos) - 1);  
	        }  
	    }  
	    return baos.toByteArray();  
	}  
	
	

    public static String encodeBase62(byte[] data) {  
        StringBuffer sb = new StringBuffer(data.length * 2);  
        int pos = 0, val = 0;  
        for (int i = 0; i < data.length; i++) {  
            val = (val << 8) | (data[i] & 0xFF);  
            pos += 8;  
            while (pos > 5) {  
                char c = encodes[val >> (pos -= 6)];  
                sb.append(  
                /**/c == 'i' ? "ia" :  
                /**/c == '+' ? "ib" :  
                /**/c == '/' ? "ic" : c);  
                val &= ((1 << pos) - 1);  
            }  
        }  
        if (pos > 0) {  
            char c = encodes[val << (6 - pos)];  
            sb.append(  
            /**/c == 'i' ? "ia" :  
            /**/c == '+' ? "ib" :  
            /**/c == '/' ? "ic" : c);  
        }  
        return sb.toString();  
    }  
      
    public static byte[] decodeBase62(char[] data) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);  
        int pos = 0, val = 0;  
        for (int i = 0; i < data.length; i++) {  
            char c = data[i];  
            if (c == 'i') {  
                c = data[++i];  
                c =  
                /**/c == 'a' ? 'i' :  
                /**/c == 'b' ? '+' :  
                /**/c == 'c' ? '/' : data[--i];  
            }  
            val = (val << 6) | decodes[c];  
            pos += 6;  
            while (pos > 7) {  
                baos.write(val >> (pos -= 8));  
                val &= ((1 << pos) - 1);  
            }  
        }  
        return baos.toByteArray();  
    }  
	
	/**
	 * Hex编码.
	 */
	public static String encodeHex(byte[] input) {
		return new String(Hex.encodeHex(input));
	}

	/**
	 * Hex解码.
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(String input) {
		try {
			return new String(Base64.encodeBase64(input.getBytes(DEFAULT_URL_ENCODING)));
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

//	/**
//	 * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
//	 */
//	public static String encodeUrlSafeBase64(byte[] input) {
//		return Base64.encodeBase64URLSafe(input);
//	}

	/**
	 * Base64解码.
	 */
	public static byte[] decodeBase64(String input) {
		try {
			return Base64.decodeBase64(input.getBytes(DEFAULT_URL_ENCODING));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * Base64解码.
	 */
	public static String decodeBase64String(String input) {
		try {
			return new String(Base64.decodeBase64(input.getBytes()), DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * Base62编码。
	 */
	public static String enBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}

	/**
	 * Html 转码.
	 *//*
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	*//**
	 * Html 解码.
	 *//*
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	*//**
	 * Xml 转码.
	 *//*
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml10(xml);
	}
*/
	/**
	 * Xml 解码.
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * URL 编码, Encode默认为UTF-8. 
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * URL 解码, Encode默认为UTF-8. 
	 */
	public static String urlDecode(String part) {

		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
