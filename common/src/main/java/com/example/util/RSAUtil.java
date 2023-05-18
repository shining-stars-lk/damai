package com.example.util;


import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: RSAUtil
 * @Description:
 * @author lk
 * @date 2023-5-18
 */
public class RSAUtil {
	
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
	 * 	生成 rsa 公钥私钥
	 * @throws NoSuchAlgorithmException 
	 */
	public static Map<String, String> createKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		String pubkey = new BASE64Encoder().encodeBuffer(publicKey.getEncoded()).replace("\r\n", "").replace("\n", "");
		String prikey = new BASE64Encoder().encodeBuffer(privateKey.getEncoded()).replace("\r\n", "").replace("\n", "");
		Map<String, String> map = new HashMap<String, String>();
		map.put(PUBLIC_KEY, pubkey);
		map.put(PRIVATE_KEY, prikey);
		return map;
	}
	
	
	
	public static Map<String, String> createKey(int size) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGenerator.initialize(size);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		String pubkey = new BASE64Encoder().encodeBuffer(publicKey.getEncoded()).replace("\r\n", "").replace("\n", "");
		String prikey = new BASE64Encoder().encodeBuffer(privateKey.getEncoded()).replace("\r\n", "").replace("\n", "");
		Map<String, String> map = new HashMap<String, String>();
		map.put(PUBLIC_KEY, pubkey);
		map.put(PRIVATE_KEY, prikey);
		return map;
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
	public static String decryptByStrkey(String data, String privateKeyStr) throws Exception {
		PrivateKey privateKey2 = RSAUtil.getPrivateKey(privateKeyStr);
		String decryptstr = RSAUtil.decrypt(data, privateKey2);
		return decryptstr;
	}
	
	/**
	 * 根据公钥串对数据进行RSA加密
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String publicKey) throws Exception {
		PublicKey publicKeyTmp = RSAUtil.getPublicKey(publicKey);
		return encrypt(data,publicKeyTmp);
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

		// String charset = "utf-8";

		// 支付宝生成器生成 2048
		 String privateKey =
		 "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCCz1KFk38uRIPS52S9i89KRTpoO5iJAUTTMO9BasfJxIVLIE+PhZEIsv2yYEPtmF6EKMWL2AVCFPJe27Wzl2ur41QMo9dVfOxuL38uIBjeW1P2Nn7LIzW+OPP+k65YsXPDWUG2tknL5kJ+mAnwHMib3TjjrdWrGF/e8itIbDdpS1mpx0WdS5ATp0ijoV0bk5PENZwk4uC1Wz2fFUDzPckx8RQeO2ifn8ZTqS6/GrItr3+EgsXBXwNIz2RMlRcFjkkCys/y+/Ox/H1oc4rDzIdMHGLP2sGZDViOhY1YnEovya086IqEpV2/SRVYSmH00mc+rz+Y/O0z/UP4014KEk+LAgMBAAECggEAYmrRz6Y1nf32oItwBBxYjaygUSPlk87clXjPygwo3Ol60BczROxdPopMzhQXh3+IPS7f0mNpMVrJMA1mHATlAaI77nam4K7ow84AhheoHkPgW/KMWxHWsZPwH63FEv4P9v4Erw9fXoWTqeOCNqGxXXOIBeUYSW5BU2AoqIii+BiaBPaPuWfPz9Ux3f+3AJTTxi8sEeZaKzG/6SKBkJWDs+/y+14wVJfOeuenkyTkSoyDBmqGS+45K3IhNIFHXGJEFCFii6Aw0EfAj9HrdSXoIBBpqAzdS83QJonCtckb0gPae2iMSWc5Z2SVZ7gD5VHQfX7Z8UxLrWc2sY8jY1aDoQKBgQDnokjE16HIGv9BNr0PoM3bGv6NATKyk39HbzBHw2USE8wyLBF7Ag7X+Seq4f8CWfNZLhfpklWEDjl9cdRNpj49T2+ei4mtZISYc5UY3pi8YNXkVNxEB2p8hJekxpxHGPqCuYha+lCe9BntDgNKzsGNX8IxgOSFfKYrNLLeY0cNXwKBgQCQke1AieWgY/TWwC6FLR+0aKeWCfyAB+vP1eA5ci62MkpPpJy2FvjF4nA5RfswUWpME9mL95ssu2OEQgQ9aXry5FF1vpKA3UJPXvpBmFsT9/Yiz4ZypEo5cuYSmx4txUx+r/fqADrwBQgsPADLccu0+H5+Akx2yNUHplZCtQKBVQKBgE2VZqg/zVlX6Y0vR233n1u3E/GoqBF2I9iKXnJ6+NxwMyHO7t/zlYMzWdz7q01fUYnaTXoXYHtFxG3G9aQ4fXpNpAZHmIqyM1NYy2S0EhZunNXIsqo6jak9RAmV2Hxb2nR0rj5Cv0QuYIys999TlzLCEyo/kMtBL+si0JJ6UC8jAoGAWnem4vMoCdVjGpvKwLX7GBdBf+eh5bCPRVDObsRgLM2NqQcWte/sH1DS71Q2mwbnoNHvGeKJbVWaMeJp6tA+Cw8LVsY5wYA0BZ0mt7OSkw1hLml09fqPJCIdjVkaaoQDmWeemQUp3JcZhSzLRoRgeE/kQBBKLy7/+DiD1FyXQ70CgYEAqhbgf1dXaRgzmfPq2ABmCEecXyGGsfXyF37rPobCHx5wACkdMce30DMxQtIkXj9JCVmb9aVx0PTsw4ETQwWhVnXcnvgCxIGBGgEPaitm99w79QQ8JUouSHnQdGu0JJIrq0mwrTPRXJsvmgGK5HMgFfa+aLdL4D7IBsk8O1TRAa8=";
		 String publicKey =
		 "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgs9ShZN/LkSD0udkvYvPSkU6aDuYiQFE0zDvQWrHycSFSyBPj4WRCLL9smBD7ZhehCjFi9gFQhTyXtu1s5drq+NUDKPXVXzsbi9/LiAY3ltT9jZ+yyM1vjjz/pOuWLFzw1lBtrZJy+ZCfpgJ8BzIm904463Vqxhf3vIrSGw3aUtZqcdFnUuQE6dIo6FdG5OTxDWcJOLgtVs9nxVA8z3JMfEUHjton5/GU6kuvxqyLa9/hILFwV8DSM9kTJUXBY5JAsrP8vvzsfx9aHOKw8yHTBxiz9rBmQ1YjoWNWJxKL8mtPOiKhKVdv0kVWEph9NJnPq8/mPztM/1D+NNeChJPiwIDAQAB";
		// 支付宝生成器生成 1024
		// String privateKey =
		// "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMnW0aI+stXm0cyz7BxQUOURHcdHoB7oT8XrznQGoWPPIi7T6b+BUCVIFWvJulsZ0axQdw7iI8qHdphSMlc4yVk+XZzVYU30BO7kRtLwYKPVc59wG7/R4Dd1gdRW+Ote+e+kdtGfk9CwpfPadpxFAFIHOFtjL/+3kpTw8rAAGr/XAgMBAAECgYEAxhEAdu6AIFf52Z9WPBK0z5UUW9J11t6yKc9VMZawGXnSWZGq+Dq1o2AxZSz4qx2THD16GCjiZoS4ZJGExQL80dCKzC7vhvM95nQZ+o22pOYwPklNGUvheRiS7T0Zg+xTfwyW8GqNEb+DcFEh1dTnoIxwaJAPA/PdgEHV/4MPR0ECQQDpwjI+tMkVje1u3Hf2A83Hba6aWposOcoYoxCP20K0+KT9NcYmW+ncICZ2fbAI5zqSL9e7AASHCHxyPRnlPUWzAkEA3QskC6giAwkB1TPn/N+k3gM/fEHabQTkCj+1ermpiFDLZr6e4IA8YPggKy9BCtgjckpx4sBpvj4pT8bvtj2TTQJBAKfavuGwR8SmiZxVy/5odNeDtyE3dgXT6X8PeNEUDS6ObUs11fetgJyEqZnkXwfFopVerXamaJneSh5uKa4m9WkCQC/K6t5CWihB2E2azXwVdvYxyZY6Ptn3hr7Yi4qffhJlNbVfXca4s1VcEMiFjRClLvTVN86JP9PMaO5A2y+SHLkCQE6GgTkSZeemIDixJts+22gqPKF5Kl+oGLLX6YSnX47R8C/MWAZvaO6Dl3B4+UAr9TarAx40HSHbanYRgBTgDcY=";
		//String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJ1tGiPrLV5tHMs+wcUFDlER3HR6Ae6E/F6850BqFjzyIu0+m/gVAlSBVrybpbGdGsUHcO4iPKh3aYUjJXOMlZPl2c1WFN9ATu5EbS8GCj1XOfcBu/0eA3dYHUVvjrXvnvpHbRn5PQsKXz2nacRQBSBzhbYy//t5KU8PKwABq/1wIDAQAB";
		String content = "万瓦鳞鳞若火龙,日车不动汗珠融.无因羽翮氛埃外,坐觉蒸炊釜甑中.";
		//String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCCz1KFk38uRIPS52S9i89KRTpoO5iJAUTTMO9BasfJxIVLIE+PhZEIsv2yYEPtmF6EKMWL2AVCFPJe27Wzl2ur41QMo9dVfOxuL38uIBjeW1P2Nn7LIzW+OPP+k65YsXPDWUG2tknL5kJ+mAnwHMib3TjjrdWrGF/e8itIbDdpS1mpx0WdS5ATp0ijoV0bk5PENZwk4uC1Wz2fFUDzPckx8RQeO2ifn8ZTqS6/GrItr3+EgsXBXwNIz2RMlRcFjkkCys/y+/Ox/H1oc4rDzIdMHGLP2sGZDViOhY1YnEovya086IqEpV2/SRVYSmH00mc+rz+Y/O0z/UP4014KEk+LAgMBAAECggEAYmrRz6Y1nf32oItwBBxYjaygUSPlk87clXjPygwo3Ol60BczROxdPopMzhQXh3+IPS7f0mNpMVrJMA1mHATlAaI77nam4K7ow84AhheoHkPgW/KMWxHWsZPwH63FEv4P9v4Erw9fXoWTqeOCNqGxXXOIBeUYSW5BU2AoqIii+BiaBPaPuWfPz9Ux3f+3AJTTxi8sEeZaKzG/6SKBkJWDs+/y+14wVJfOeuenkyTkSoyDBmqGS+45K3IhNIFHXGJEFCFii6Aw0EfAj9HrdSXoIBBpqAzdS83QJonCtckb0gPae2iMSWc5Z2SVZ7gD5VHQfX7Z8UxLrWc2sY8jY1aDoQKBgQDnokjE16HIGv9BNr0PoM3bGv6NATKyk39HbzBHw2USE8wyLBF7Ag7X+Seq4f8CWfNZLhfpklWEDjl9cdRNpj49T2+ei4mtZISYc5UY3pi8YNXkVNxEB2p8hJekxpxHGPqCuYha+lCe9BntDgNKzsGNX8IxgOSFfKYrNLLeY0cNXwKBgQCQke1AieWgY/TWwC6FLR+0aKeWCfyAB+vP1eA5ci62MkpPpJy2FvjF4nA5RfswUWpME9mL95ssu2OEQgQ9aXry5FF1vpKA3UJPXvpBmFsT9/Yiz4ZypEo5cuYSmx4txUx+r/fqADrwBQgsPADLccu0+H5+Akx2yNUHplZCtQKBVQKBgE2VZqg/zVlX6Y0vR233n1u3E/GoqBF2I9iKXnJ6+NxwMyHO7t/zlYMzWdz7q01fUYnaTXoXYHtFxG3G9aQ4fXpNpAZHmIqyM1NYy2S0EhZunNXIsqo6jak9RAmV2Hxb2nR0rj5Cv0QuYIys999TlzLCEyo/kMtBL+si0JJ6UC8jAoGAWnem4vMoCdVjGpvKwLX7GBdBf+eh5bCPRVDObsRgLM2NqQcWte/sH1DS71Q2mwbnoNHvGeKJbVWaMeJp6tA+Cw8LVsY5wYA0BZ0mt7OSkw1hLml09fqPJCIdjVkaaoQDmWeemQUp3JcZhSzLRoRgeE/kQBBKLy7/+DiD1FyXQ70CgYEAqhbgf1dXaRgzmfPq2ABmCEecXyGGsfXyF37rPobCHx5wACkdMce30DMxQtIkXj9JCVmb9aVx0PTsw4ETQwWhVnXcnvgCxIGBGgEPaitm99w79QQ8JUouSHnQdGu0JJIrq0mwrTPRXJsvmgGK5HMgFfa+aLdL4D7IBsk8O1TRAa8=";
		// // #RSA
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("app", "100014");
		// map.put("partner", "jh1000000");
		// map.put("charset", "utf-8");
		//
		// String qqq =
		// "app=100014&partner=jh1000000&charset=utf-8&bizContent=1&timestamp=201909051633";
		//
		// String sign = RSA.rsaSign(content, privateKey, charset,
		// SIGN_SHA256RSA_ALGORITHMS);
		// System.out.println("RSA SIGN:" + sign);
		// boolean verifyFlag = RSA.rsaCheck(content, sign, publicKey, charset,
		// SIGN_SHA256RSA_ALGORITHMS);
		// System.out.println("RSA VERIFY FLAG:" + verifyFlag);

		// #RSA2
		// String sign2 = RSA.rsaSign(content, privateKey, charset, SIGN_TYPE_RSA2);
		// System.out.println("RSA2 SIGN:" + sign2);
		// boolean verifyFlag2 = RSA.rsaCheck(content, sign2, publicKey, charset,
		// SIGN_TYPE_RSA2);
		// System.out.println("RSA2 VERIFY FLAG:" + verifyFlag2 );
//		String  privateKeystring="MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCotoq6y1SFw8aE0cnRdK1XS4cpAWWqk+5+49u8Eq/JIr/mgn5aaYCf1AXbvGAsFbYz+FAn/8+gxelG1ZlYE+afhwWIJgHVIVk+pNftUz88pzDCIWCgLUTx34aVayYc6o+m7JwfMWzuEmIL5hrHW7SpXpecQvikyWhPCoIR0GvtFGqgkWkVRFzuJTFfT5V/OXNUp4upXNGBAmmRhM6oHp5TTDvO1L29Dg01EFbAJN8peRz1gm/kftM1C7WKPbX1EB+E/UudoG+94m3tiSWukNbG9w85dNxN4wdY3qg8hAOqbwz4iWQ8TxmWhESV5CHI5BfMZz1jrZat9+hkq5sj6bVJAgMBAAECggEAT6D4kXluh0QD6qRpsOZ0+gK35G64YW443sME7xuSxEgWcuGcaWfqQYV8zl12ttf4i0bj7j0a7jL7C2e9XIjoDNtZjQ0DSGjZCmwbTfRAoIv4w2elh3RWWiK6DsPn1TYQXyTF+yME2sjqZdtRQE7UUixHOoWRfZViGgxHn8jTZ623p6OqvEfihwlY/hH9eNZAqMg5X8IYZa03dgOPan03MI6js2ecX98XPg1wRA+GC9DwWNB1A3Afggpr9eMqAukPZXYNdjIN55yOJk+PTcHsK/vhSp+EuN7wziRQ8IdQuQhSVcikXJljLdIu8KZO3A8WjE3S9lLFo3DqpRiJYf3akQKBgQD6H9qrfarWS0bJdcQnI9c4TfmFOuAtIvyjRFL84jQmdgg9Ocl/5kCOu3Xv5GTFVjDloRQrMaU5N6zZh25fSokj/01P+fqcgZC4I28EW4O1N31/iWhLbwZWVGHek0m9yaND4PTEX6ctS86MdtNyNMPX7MHP2TKk1IbRwOTdvk1J9wKBgQCsrRzy5Dc/VlTSYeOoy/nFtd0Mn1Iqzt0cxyDCMmU1C0QoaOu6QGDq2dVXpp5qcO+X0OX+WgUtH3xW3FDx6StcuvlrImkLM8H8vhqdBsR7p/jWxrge65w47+uH+Vbdftz44MzrCGf3Wvx1Qlg6w9jak+Yv5gA5qyxP9fML94sqvwKBgDpOJJ4nPCSaBZU+s4vgKfJBEvJAqDM9XaD9c07pt/hjOUZ0awk6dkAPaM2T8v2hOuK8asQq3XX6CXvnuw3RhSh0AeSrxASpmz+hWuasIoaiucl6EWrMJbEIGIxWzzDDiSBwlv4Twhen/Sz3IhEURbbWxNMbsVeFByxsB629B60/AoGAISKXC4T/lQfb/wMJzLpXk7jyUhnAirZnd74+awWApk0jJx37HiJ8dpSWe9tdSs+hKmDLwaNEmwQPuBtO7RzxVToL4qMXxS6JaxaPxNInohL5jT1U15oewE5JxC30nUxa2udIa1GRocWz7Exu4dfzJP58jXR1VpuiRTXNDd7StcECgYBUfvrasEl+H1NB7lXH32GFnh5U0RXqx9R2Liwta+kz5BGcCQvjIuH0sgcBwiFzy0udgEuUrBgSbT5Wtz0abmlyjR7V+DoiEb4X9c0ceKBtNBW2Y32cUOkoJpUrmIBGO8Yw3Y98imTFpF3N7LJhmHJFdtLL7wrNeMp8NgoUf4pJbg==";
//		String  publicKeystring="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqLaKustUhcPGhNHJ0XStV0uHKQFlqpPufuPbvBKvySK/5oJ+WmmAn9QF27xgLBW2M/hQJ//PoMXpRtWZWBPmn4cFiCYB1SFZPqTX7VM/PKcwwiFgoC1E8d+GlWsmHOqPpuycHzFs7hJiC+Yax1u0qV6XnEL4pMloTwqCEdBr7RRqoJFpFURc7iUxX0+VfzlzVKeLqVzRgQJpkYTOqB6eU0w7ztS9vQ4NNRBWwCTfKXkc9YJv5H7TNQu1ij219RAfhP1LnaBvveJt7YklrpDWxvcPOXTcTeMHWN6oPIQDqm8M+IlkPE8ZloREleQhyOQXzGc9Y62WrffoZKubI+m1SQIDAQAB";
		String  privateKeystring="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQJkwEsAuN/LUVOcCbxOR/TGT44StKdl/4fAa+0ZdA/KETSc9JOETVVLERpCUdWyXk8i08WoFauoQMJ60PPET52Ybc5DkTT0WLSiI3ZiQ/JqOS7yZT9SU0IgWCwPpfTl3gHpW3ghyjuYbh5CDfjtI5E+RMdxZad7F9a676Mq3Upw8w4QbEFjMM9t+YSRix0wINn4D8P3uIZaMKV2Lka7REwDzDvozwpBBUSUUS4leZ+D/OMROKvGkU9Xo3+U+BJ3RxFJEKAsRjLhF2Gyr98eNvJZZGCZW4qGvG1GUDow9/YPgRQd/6xyrnXhhFfTE+FYkwkSdd3/zgA4tED5mlvqo5AgMBAAECggEAbrVK+uqt5R2pgaS6PagJCY+FmPNuSSEC8hdnItRbgyjDGMo+LfDORL1nxLG5Y4NmZiBtB4ZHBIRs99zc9++E1hLvleSdxSvDsId+IegLfIjTZfzQBfkn2pMqglJtuBa62G8E80w1bLUHV6Pab3gEEpwpYWSwwUheA4JZf4S+pqt+WHewEgY33fHW40UMPOh1rNgX6kCBD2caAbD0XPkqyAmjeRjbL9HB4hXPiR2jzsznbph5EB91Lbd/VYnEj12EZBfPiVLuv10VJ8SEVos0j2XTeIaoie3u0A2irBW/2LSED6BCLT2Ef7voLYkvBZm0hgRtZDd2ypKS0Nbrs2guAQKBgQDq7WoHVJeg9/4S0Z1q/PDvwna/iZ3DcsA2SRcCp1CsMqLymwRFB5RefQr309HNmTyWLJYld1nQQbweFMC3qTjjKtubIFtGM2xQu+VEKyyTKHYpmogvze9g53BfGJ0aRSzCBIMrdRcg7i+adD/EQNh880pXnbq9g8jropunLSJXQQKBgQCdFF9J40DQwJmAg52OjzJJcN++23B72ZvV3Fm9e6buiENTg4ns946a8FMZ0NjsNBwHPSeZRY6gQ+TBzIRxKa5KTfKCa93snXHy6bd1w3Vwxf0xKRTNzEuTOLid8ZzdF2SFNaQPWQSL2Kiqb4ibJE5vyAbBOKgrYG7xS+fWPdDM+QKBgQClgrZci8B367/Yyk05fxLQGBVJRE8dz86TcFMBz5cKenJKseQELh7dowMDLvx0+SyyMJ9nrfpcurMqDjzoKBSoFx244MbtYnVBO99+c1xoIO1+yhyAcXngr61/kfu88ldiKYOwLkIpNSXcj2INhhH/lfgeWkWiFyKKwHkhAq9bAQKBgClXsFDBKFEJMB2cBn77/29G7wzLsspWFubtzQzLG7+SS64SY93jtjvfXu/fgQiCGpJpLR/xJ73UwMOlMYKbRs01wMtE2q8pLVGS4/9xx1o8MgybVGdqCsYf2q9vZSrnukQ5PwnvEvw/tPzOyDSVFLPlAKogYFqyUeDeapF/yZH5AoGAT2tOEG4WGs+PMFasvxcJYj+EVkHsM11FvjEdl1ZxR3jcNwYs+JArgrdGif9cg+hW4VlqbTrZ6gALXLflBfZ56ghClm4Icg6pwgzmsMuoj5aBP8fSLaNEeuj796xOxgN8w+tkxjlanJxDcilykpfW8H4hrVZAk2aIxZtUAMpd/bE=";
		String  publicKeystring="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkCZMBLALjfy1FTnAm8Tkf0xk+OErSnZf+HwGvtGXQPyhE0nPSThE1VSxEaQlHVsl5PItPFqBWrqEDCetDzxE+dmG3OQ5E09Fi0oiN2YkPyajku8mU/UlNCIFgsD6X05d4B6Vt4Ico7mG4eQg347SORPkTHcWWnexfWuu+jKt1KcPMOEGxBYzDPbfmEkYsdMCDZ+A/D97iGWjCldi5Gu0RMA8w76M8KQQVElFEuJXmfg/zjETirxpFPV6N/lPgSd0cRSRCgLEYy4Rdhsq/fHjbyWWRgmVuKhrxtRlA6MPf2D4EUHf+scq514YRX0xPhWJMJEnXd/84AOLRA+Zpb6qOQIDAQAB";
//		String pr="tYt/y72pqmhZNQj85t8S00qiihEuOvUzQ7Ge05LWhp5QGhD6a0nCgmm87QXQFHAgIvrtVLtFGwKBgQCN9Gz5pCfej8TK14kHMC6h9tuZYgredBrdZKXBiqfmqZ+4usyh9+2HoeV61/70HoevIWqzoNX7HVGrULp4hO9+5fzhbvKl99yEMzTvJMKM3BAeceB0lZwDzTdO0MjHxhFvaQSV62tKKTvUuxG6sMjCom5YQGIex+GKE6IWxKgWwQKBgAeVJcsweP3faMO9djdmqD+scuojx9Ntr7kvoud+jI4EQFGKLJGA0j1Tt3NVNo5dd0Q7hKsFqrg8KaR0O1NlIO0edKn4ugLfz8giRbisKsWoNP8lvW86hNjl4KYgngEYMMAZhBu+wLgaSFtEMltr6lcJnewcR5fYu48Q2sKsIcTxAoGAbsj5xx9crVK8x93T0v0lBrH/l+dg5ohMwGZoK1obRM3z74ZS8diRkIpPbzLbMaOnhuEasFRZXYgRiJiFS3MpA+2HCLIy0+ClQtQQNEShOrOZzmmN/vH5DDJurHZtlN9tY4mEckg4ayUg3lc4xMa1sBRESfaCjxFRjkByKsqLPiI=";
//		String puString="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmZNjUdjuWmPgQrgLJOudqc9ZZT8afZtkC1Nqcm8CiNwpoVC0rz9p1yfbLo76PuGc4QFx0yxthzk9ydaM6nvamqzlMarVSq2btsIHxXRTapSARryfMgQ7fur9K8asSW6lIAfHcnjVJshIoDaQqt2x7wYLOK17rG6N9BckRBv3+124MxzdDB2ppDDZH91olLDmpyc1ILfgH1nCGaELpyzCI8uu6gLIUvhpGJ357mgrr+SLPveM2Dy7viXx4FLGHO6W4kVymWWmEsbMC190Qt2VuDWGqH/8qYiadJ/aSsLQOWuUtA2A02G1E5IlfJYfEH0dQhf7oiniEg+RS6pLItOm0wIDAQAB";
		PublicKey publicKey2 = getPublicKey(publicKeystring);
		PrivateKey privateKey2 = getPrivateKey(privateKeystring);
		String encrypt = encrypt("{\"app\":\"40068980X4_1\",\"mobile\":\"13552881968\",\"type\":\"2\"}", publicKey2);
		System.out.println(encrypt);
//		String aa="SEYor1n0YpJvuxj2n/H7iAfoiFvV%2BfXWbk1h9HGMdcurccJkivAXIVvwZQhwZC1jRMVo0rtkMRGARJFfa1YoZBZByyUGWyDsJ%2BAQnXRl5OI2q3ATasZq%2BbHzrP/c9g/DNaOKwENVg0zpv4zPQeEansiHAIT2gM/%2Bx9JWzOptVXQJ4PajYFB95GLuu9ISHM%2BwgSN/0Mo6Ii6PrJkohsgd7cLNb8%2BRz8TJPwPVRXWrDHiHHtwtXU/B/VScs56I7tPemMPAhxaRO8KWByG05ltgqvos3e4eHi/cnB%2BOyhSxTgcqVs%2BaGa5MOf6/2ZusnVXLDvHRBut2J41ZAa/NJC2/dw==";
		String decrypt = decrypt(encrypt, privateKey2);
		System.out.println(decrypt);
		
		Map<String, String> createKey = createKey();
		System.out.println(createKey);
		
	}

}
