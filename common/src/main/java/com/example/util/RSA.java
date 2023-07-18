//package com.example.util;
//
//import org.springframework.util.StringUtils;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.security.KeyFactory;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.Signature;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//
///**
// * @ClassName: RSA
// * @Description:
// * @author 星哥
// * @date 2023-5-18
// */
//public class RSA {
//	private static final String SIGN_TYPE_RSA = "RSA";
//	private static final String SIGN_TYPE_RSA2 = "RSA2";
//	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
//	private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";
//	private static final int DEFAULT_BUFFER_SIZE = 8192;
//
//	/**
//	 * RSA/RSA2 生成签名
//	 * 
//	 * @param content
//	 *            待签名内容
//	 * @param privateKey
//	 *            私钥
//	 * @param charset
//	 *            编码方式
//	 * @param signType
//	 *            签名类型：RSA/RSA2
//	 * @return
//	 */
//	public static String rsaSign(String content, String privateKey, String charset, String signType) {
//		if (SIGN_TYPE_RSA.equals(signType)) {
//			return rsaSign(content, privateKey, charset);
//		} else if (SIGN_TYPE_RSA2.equals(signType)) {
//			return rsa256Sign(content, privateKey, charset);
//		} else {
//			throw new RuntimeException("Sign Type is Not Support : signType=" + signType);
//		}
//	}
//
//	/**
//	 * RSA/RSA2 验签
//	 * @param content 	已签名内容
//	 * @param sign		签名
//	 * @param publicKey	公钥
//	 * @param charset	编码方式
//	 * @param signType	签名类型：RSA/RSA2
//	 * @return
//	 */
//	public static boolean rsaCheck(String content, String sign, String publicKey, String charset, String signType) {
//		if (SIGN_TYPE_RSA.equals(signType)) {
//			return rsaCheck(content, sign, publicKey, charset);
//		} else if (SIGN_TYPE_RSA2.equals(signType)) {
//			return rsa256Check(content, sign, publicKey, charset);
//		} else {
//			throw new RuntimeException("Sign Type is Not Support : signType=" + signType);
//		}
//	}
//
//	/**
//	 * RSA生成签名
//	 * @param content
//	 * @param privateKey
//	 * @param charset
//	 * @return
//	 */
//	public static String rsaSign(String content, String privateKey, String charset) {
//		try {
//			PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
//
//			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
//
//			signature.initSign(priKey);
//
//			if (StringUtils.isEmpty(charset)) {
//				signature.update(content.getBytes());
//			} else {
//				signature.update(content.getBytes(charset));
//			}
//
//			byte[] signed = signature.sign();
//
//			return new String(Base64.encode(signed));
//		} catch (InvalidKeySpecException ie) {
//			throw new RuntimeException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", ie);
//		} catch (Exception e) {
//			throw new RuntimeException("RSAcontent = " + content + "; charset = " + charset, e);
//		}
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
//		try {
//			PublicKey pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));
//
//			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
//
//			signature.initVerify(pubKey);
//
//			if (StringUtils.isEmpty(charset)) {
//				signature.update(content.getBytes());
//			} else {
//				signature.update(content.getBytes(charset));
//			}
//
//			return signature.verify(Base64.decode(sign));
//		} catch (Exception e) {
//			throw new RuntimeException("RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
//		}
//	}
//
//	/**
//	 * RSA2生成签名
//	 * @param content
//	 * @param privateKey
//	 * @param charset
//	 * @return
//	 */
//	public static String rsa256Sign(String content, String privateKey, String charset) {
//		try {
//			PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
//
//			Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
//
//			signature.initSign(priKey);
//
//			if (StringUtils.isEmpty(charset)) {
//				signature.update(content.getBytes());
//			} else {
//				signature.update(content.getBytes(charset));
//			}
//
//			byte[] signed = signature.sign();
//
//			return new String(Base64.encode(signed));
//		} catch (Exception e) {
//			throw new RuntimeException("RSAcontent = " + content + "; charset = " + charset, e);
//		}
//
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
//		try {
//			PublicKey pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));
//
//			Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
//
//			signature.initVerify(pubKey);
//
//			if (StringUtils.isEmpty(charset)) {
//				signature.update(content.getBytes());
//			} else {
//				signature.update(content.getBytes(charset));
//			}
//
//			return signature.verify(Base64.decode(sign));
//		} catch (Exception e) {
//			throw new RuntimeException("RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
//		}
//	}
//
//	private static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
//		if (ins == null || StringUtils.isEmpty(algorithm)) {
//			return null;
//		}
//
//		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
//
//		byte[] encodedKey = readText(ins).getBytes();
//
//		encodedKey = Base64.decode(new String(encodedKey));
//
//		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
//	}
//
//	private static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
//		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
//
//		StringWriter writer = new StringWriter();
//		io(new InputStreamReader(ins), writer, -1);
//
//		byte[] encodedKey = writer.toString().getBytes();
//
//		encodedKey = Base64.decode(new String(encodedKey));
//
//		return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
//	}
//
//	private static String readText(InputStream ins) throws IOException {
//		Reader reader = new InputStreamReader(ins);
//		StringWriter writer = new StringWriter();
//		io(reader, writer, -1);
//		return writer.toString();
//	}
//
//	private static void io(Reader in, Writer out, int bufferSize) throws IOException {
//		if (bufferSize == -1) {
//			bufferSize = DEFAULT_BUFFER_SIZE >> 1;
//		}
//
//		char[] buffer = new char[bufferSize];
//		int amount;
//
//		while ((amount = in.read(buffer)) >= 0) {
//			out.write(buffer, 0, amount);
//		}
//	}
//}
