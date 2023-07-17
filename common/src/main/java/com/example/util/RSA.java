package com.example.util;

import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * @ClassName: RSA
 * @Description:
 * @author 星哥
 * @date 2023-5-18
 */
public class RSA {
	private static final String SIGN_TYPE_RSA = "RSA";
	private static final String SIGN_TYPE_RSA2 = "RSA2";
	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";
	private static final int DEFAULT_BUFFER_SIZE = 8192;

	/**
	 * RSA/RSA2 生成签名
	 * 
	 * @param content
	 *            待签名内容
	 * @param privateKey
	 *            私钥
	 * @param charset
	 *            编码方式
	 * @param signType
	 *            签名类型：RSA/RSA2
	 * @return
	 */
	public static String rsaSign(String content, String privateKey, String charset, String signType) {
		if (SIGN_TYPE_RSA.equals(signType)) {
			return rsaSign(content, privateKey, charset);
		} else if (SIGN_TYPE_RSA2.equals(signType)) {
			return rsa256Sign(content, privateKey, charset);
		} else {
			throw new RuntimeException("Sign Type is Not Support : signType=" + signType);
		}
	}
	
	/**
	 * RSA/RSA2 验签
	 * @param content 	已签名内容
	 * @param sign		签名
	 * @param publicKey	公钥
	 * @param charset	编码方式
	 * @param signType	签名类型：RSA/RSA2
	 * @return
	 */
	public static boolean rsaCheck(String content, String sign, String publicKey, String charset, String signType) {
		if (SIGN_TYPE_RSA.equals(signType)) {
			return rsaCheck(content, sign, publicKey, charset);
		} else if (SIGN_TYPE_RSA2.equals(signType)) {
			return rsa256Check(content, sign, publicKey, charset);
		} else {
			throw new RuntimeException("Sign Type is Not Support : signType=" + signType);
		}
	}
	
	/**
	 * RSA生成签名
	 * @param content
	 * @param privateKey
	 * @param charset
	 * @return
	 */
	public static String rsaSign(String content, String privateKey, String charset) {
		try {
			PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));

			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);

			if (StringUtils.isEmpty(charset)) {
				signature.update(content.getBytes());
			} else {
				signature.update(content.getBytes(charset));
			}

			byte[] signed = signature.sign();

			return new String(Base64.encode(signed));
		} catch (InvalidKeySpecException ie) {
			throw new RuntimeException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", ie);
		} catch (Exception e) {
			throw new RuntimeException("RSAcontent = " + content + "; charset = " + charset, e);
		}
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
		try {
			PublicKey pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));

			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);

			if (StringUtils.isEmpty(charset)) {
				signature.update(content.getBytes());
			} else {
				signature.update(content.getBytes(charset));
			}

			return signature.verify(Base64.decode(sign));
		} catch (Exception e) {
			throw new RuntimeException("RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
		}
	}

	/**
	 * RSA2生成签名
	 * @param content
	 * @param privateKey
	 * @param charset
	 * @return
	 */
	public static String rsa256Sign(String content, String privateKey, String charset) {
		try {
			PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));

			Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);

			signature.initSign(priKey);

			if (StringUtils.isEmpty(charset)) {
				signature.update(content.getBytes());
			} else {
				signature.update(content.getBytes(charset));
			}

			byte[] signed = signature.sign();

			return new String(Base64.encode(signed));
		} catch (Exception e) {
			throw new RuntimeException("RSAcontent = " + content + "; charset = " + charset, e);
		}

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
		try {
			PublicKey pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));

			Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);

			signature.initVerify(pubKey);

			if (StringUtils.isEmpty(charset)) {
				signature.update(content.getBytes());
			} else {
				signature.update(content.getBytes(charset));
			}

			return signature.verify(Base64.decode(sign));
		} catch (Exception e) {
			throw new RuntimeException("RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
		}
	}

	private static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
		if (ins == null || StringUtils.isEmpty(algorithm)) {
			return null;
		}

		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

		byte[] encodedKey = readText(ins).getBytes();

		encodedKey = Base64.decode(new String(encodedKey));

		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
	}
	
	private static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

		StringWriter writer = new StringWriter();
		io(new InputStreamReader(ins), writer, -1);

		byte[] encodedKey = writer.toString().getBytes();

		encodedKey = Base64.decode(new String(encodedKey));

		return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
	}

	private static String readText(InputStream ins) throws IOException {
		Reader reader = new InputStreamReader(ins);
		StringWriter writer = new StringWriter();
		io(reader, writer, -1);
		return writer.toString();
	}

	private static void io(Reader in, Writer out, int bufferSize) throws IOException {
		if (bufferSize == -1) {
			bufferSize = DEFAULT_BUFFER_SIZE >> 1;
		}

		char[] buffer = new char[bufferSize];
		int amount;

		while ((amount = in.read(buffer)) >= 0) {
			out.write(buffer, 0, amount);
		}
	}
	public static void main(String[] args) throws InvalidKeyException, Exception {
//		String charset = "utf-8";
//		
////		支付宝生成器生成 2048
////		String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCCz1KFk38uRIPS52S9i89KRTpoO5iJAUTTMO9BasfJxIVLIE+PhZEIsv2yYEPtmF6EKMWL2AVCFPJe27Wzl2ur41QMo9dVfOxuL38uIBjeW1P2Nn7LIzW+OPP+k65YsXPDWUG2tknL5kJ+mAnwHMib3TjjrdWrGF/e8itIbDdpS1mpx0WdS5ATp0ijoV0bk5PENZwk4uC1Wz2fFUDzPckx8RQeO2ifn8ZTqS6/GrItr3+EgsXBXwNIz2RMlRcFjkkCys/y+/Ox/H1oc4rDzIdMHGLP2sGZDViOhY1YnEovya086IqEpV2/SRVYSmH00mc+rz+Y/O0z/UP4014KEk+LAgMBAAECggEAYmrRz6Y1nf32oItwBBxYjaygUSPlk87clXjPygwo3Ol60BczROxdPopMzhQXh3+IPS7f0mNpMVrJMA1mHATlAaI77nam4K7ow84AhheoHkPgW/KMWxHWsZPwH63FEv4P9v4Erw9fXoWTqeOCNqGxXXOIBeUYSW5BU2AoqIii+BiaBPaPuWfPz9Ux3f+3AJTTxi8sEeZaKzG/6SKBkJWDs+/y+14wVJfOeuenkyTkSoyDBmqGS+45K3IhNIFHXGJEFCFii6Aw0EfAj9HrdSXoIBBpqAzdS83QJonCtckb0gPae2iMSWc5Z2SVZ7gD5VHQfX7Z8UxLrWc2sY8jY1aDoQKBgQDnokjE16HIGv9BNr0PoM3bGv6NATKyk39HbzBHw2USE8wyLBF7Ag7X+Seq4f8CWfNZLhfpklWEDjl9cdRNpj49T2+ei4mtZISYc5UY3pi8YNXkVNxEB2p8hJekxpxHGPqCuYha+lCe9BntDgNKzsGNX8IxgOSFfKYrNLLeY0cNXwKBgQCQke1AieWgY/TWwC6FLR+0aKeWCfyAB+vP1eA5ci62MkpPpJy2FvjF4nA5RfswUWpME9mL95ssu2OEQgQ9aXry5FF1vpKA3UJPXvpBmFsT9/Yiz4ZypEo5cuYSmx4txUx+r/fqADrwBQgsPADLccu0+H5+Akx2yNUHplZCtQKBVQKBgE2VZqg/zVlX6Y0vR233n1u3E/GoqBF2I9iKXnJ6+NxwMyHO7t/zlYMzWdz7q01fUYnaTXoXYHtFxG3G9aQ4fXpNpAZHmIqyM1NYy2S0EhZunNXIsqo6jak9RAmV2Hxb2nR0rj5Cv0QuYIys999TlzLCEyo/kMtBL+si0JJ6UC8jAoGAWnem4vMoCdVjGpvKwLX7GBdBf+eh5bCPRVDObsRgLM2NqQcWte/sH1DS71Q2mwbnoNHvGeKJbVWaMeJp6tA+Cw8LVsY5wYA0BZ0mt7OSkw1hLml09fqPJCIdjVkaaoQDmWeemQUp3JcZhSzLRoRgeE/kQBBKLy7/+DiD1FyXQ70CgYEAqhbgf1dXaRgzmfPq2ABmCEecXyGGsfXyF37rPobCHx5wACkdMce30DMxQtIkXj9JCVmb9aVx0PTsw4ETQwWhVnXcnvgCxIGBGgEPaitm99w79QQ8JUouSHnQdGu0JJIrq0mwrTPRXJsvmgGK5HMgFfa+aLdL4D7IBsk8O1TRAa8=";
////		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgs9ShZN/LkSD0udkvYvPSkU6aDuYiQFE0zDvQWrHycSFSyBPj4WRCLL9smBD7ZhehCjFi9gFQhTyXtu1s5drq+NUDKPXVXzsbi9/LiAY3ltT9jZ+yyM1vjjz/pOuWLFzw1lBtrZJy+ZCfpgJ8BzIm904463Vqxhf3vIrSGw3aUtZqcdFnUuQE6dIo6FdG5OTxDWcJOLgtVs9nxVA8z3JMfEUHjton5/GU6kuvxqyLa9/hILFwV8DSM9kTJUXBY5JAsrP8vvzsfx9aHOKw8yHTBxiz9rBmQ1YjoWNWJxKL8mtPOiKhKVdv0kVWEph9NJnPq8/mPztM/1D+NNeChJPiwIDAQAB";
////		支付宝生成器生成 1024
////		String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMnW0aI+stXm0cyz7BxQUOURHcdHoB7oT8XrznQGoWPPIi7T6b+BUCVIFWvJulsZ0axQdw7iI8qHdphSMlc4yVk+XZzVYU30BO7kRtLwYKPVc59wG7/R4Dd1gdRW+Ote+e+kdtGfk9CwpfPadpxFAFIHOFtjL/+3kpTw8rAAGr/XAgMBAAECgYEAxhEAdu6AIFf52Z9WPBK0z5UUW9J11t6yKc9VMZawGXnSWZGq+Dq1o2AxZSz4qx2THD16GCjiZoS4ZJGExQL80dCKzC7vhvM95nQZ+o22pOYwPklNGUvheRiS7T0Zg+xTfwyW8GqNEb+DcFEh1dTnoIxwaJAPA/PdgEHV/4MPR0ECQQDpwjI+tMkVje1u3Hf2A83Hba6aWposOcoYoxCP20K0+KT9NcYmW+ncICZ2fbAI5zqSL9e7AASHCHxyPRnlPUWzAkEA3QskC6giAwkB1TPn/N+k3gM/fEHabQTkCj+1ermpiFDLZr6e4IA8YPggKy9BCtgjckpx4sBpvj4pT8bvtj2TTQJBAKfavuGwR8SmiZxVy/5odNeDtyE3dgXT6X8PeNEUDS6ObUs11fetgJyEqZnkXwfFopVerXamaJneSh5uKa4m9WkCQC/K6t5CWihB2E2azXwVdvYxyZY6Ptn3hr7Yi4qffhJlNbVfXca4s1VcEMiFjRClLvTVN86JP9PMaO5A2y+SHLkCQE6GgTkSZeemIDixJts+22gqPKF5Kl+oGLLX6YSnX47R8C/MWAZvaO6Dl3B4+UAr9TarAx40HSHbanYRgBTgDcY=";
//		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJ1tGiPrLV5tHMs+wcUFDlER3HR6Ae6E/F6850BqFjzyIu0+m/gVAlSBVrybpbGdGsUHcO4iPKh3aYUjJXOMlZPl2c1WFN9ATu5EbS8GCj1XOfcBu/0eA3dYHUVvjrXvnvpHbRn5PQsKXz2nacRQBSBzhbYy//t5KU8PKwABq/1wIDAQAB";
//		String content = "万瓦鳞鳞若火龙,日车不动汗珠融.无因羽翮氛埃外,坐觉蒸炊釜甑中.";
//		String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCCz1KFk38uRIPS52S9i89KRTpoO5iJAUTTMO9BasfJxIVLIE+PhZEIsv2yYEPtmF6EKMWL2AVCFPJe27Wzl2ur41QMo9dVfOxuL38uIBjeW1P2Nn7LIzW+OPP+k65YsXPDWUG2tknL5kJ+mAnwHMib3TjjrdWrGF/e8itIbDdpS1mpx0WdS5ATp0ijoV0bk5PENZwk4uC1Wz2fFUDzPckx8RQeO2ifn8ZTqS6/GrItr3+EgsXBXwNIz2RMlRcFjkkCys/y+/Ox/H1oc4rDzIdMHGLP2sGZDViOhY1YnEovya086IqEpV2/SRVYSmH00mc+rz+Y/O0z/UP4014KEk+LAgMBAAECggEAYmrRz6Y1nf32oItwBBxYjaygUSPlk87clXjPygwo3Ol60BczROxdPopMzhQXh3+IPS7f0mNpMVrJMA1mHATlAaI77nam4K7ow84AhheoHkPgW/KMWxHWsZPwH63FEv4P9v4Erw9fXoWTqeOCNqGxXXOIBeUYSW5BU2AoqIii+BiaBPaPuWfPz9Ux3f+3AJTTxi8sEeZaKzG/6SKBkJWDs+/y+14wVJfOeuenkyTkSoyDBmqGS+45K3IhNIFHXGJEFCFii6Aw0EfAj9HrdSXoIBBpqAzdS83QJonCtckb0gPae2iMSWc5Z2SVZ7gD5VHQfX7Z8UxLrWc2sY8jY1aDoQKBgQDnokjE16HIGv9BNr0PoM3bGv6NATKyk39HbzBHw2USE8wyLBF7Ag7X+Seq4f8CWfNZLhfpklWEDjl9cdRNpj49T2+ei4mtZISYc5UY3pi8YNXkVNxEB2p8hJekxpxHGPqCuYha+lCe9BntDgNKzsGNX8IxgOSFfKYrNLLeY0cNXwKBgQCQke1AieWgY/TWwC6FLR+0aKeWCfyAB+vP1eA5ci62MkpPpJy2FvjF4nA5RfswUWpME9mL95ssu2OEQgQ9aXry5FF1vpKA3UJPXvpBmFsT9/Yiz4ZypEo5cuYSmx4txUx+r/fqADrwBQgsPADLccu0+H5+Akx2yNUHplZCtQKBVQKBgE2VZqg/zVlX6Y0vR233n1u3E/GoqBF2I9iKXnJ6+NxwMyHO7t/zlYMzWdz7q01fUYnaTXoXYHtFxG3G9aQ4fXpNpAZHmIqyM1NYy2S0EhZunNXIsqo6jak9RAmV2Hxb2nR0rj5Cv0QuYIys999TlzLCEyo/kMtBL+si0JJ6UC8jAoGAWnem4vMoCdVjGpvKwLX7GBdBf+eh5bCPRVDObsRgLM2NqQcWte/sH1DS71Q2mwbnoNHvGeKJbVWaMeJp6tA+Cw8LVsY5wYA0BZ0mt7OSkw1hLml09fqPJCIdjVkaaoQDmWeemQUp3JcZhSzLRoRgeE/kQBBKLy7/+DiD1FyXQ70CgYEAqhbgf1dXaRgzmfPq2ABmCEecXyGGsfXyF37rPobCHx5wACkdMce30DMxQtIkXj9JCVmb9aVx0PTsw4ETQwWhVnXcnvgCxIGBGgEPaitm99w79QQ8JUouSHnQdGu0JJIrq0mwrTPRXJsvmgGK5HMgFfa+aLdL4D7IBsk8O1TRAa8=";
////		#RSA
//		String sign = RSA.rsaSign(content, privateKey, charset, SIGN_TYPE_RSA);
//		System.out.println("RSA SIGN:" + sign);
//		boolean verifyFlag = RSA.rsaCheck(content, sign, publicKey, charset, SIGN_TYPE_RSA);
//		System.out.println("RSA VERIFY FLAG:" + verifyFlag );
//		
////		#RSA2
//		String sign2 = RSA.rsaSign(content, privateKey, charset, SIGN_TYPE_RSA2);
//		System.out.println("RSA2 SIGN:" + sign2);
//		boolean verifyFlag2 = RSA.rsaCheck(content, sign2, publicKey, charset, SIGN_TYPE_RSA2);
//		System.out.println("RSA2 VERIFY FLAG:" + verifyFlag2 );
		
		String str ="";
		String publicKeys = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkCZMBLALjfy1FTnAm8Tkf0xk+OErSnZf+HwGvtGXQPyhE0nPSThE1VSxEaQlHVsl5PItPFqBWrqEDCetDzxE+dmG3OQ5E09Fi0oiN2YkPyajku8mU/UlNCIFgsD6X05d4B6Vt4Ico7mG4eQg347SORPkTHcWWnexfWuu+jKt1KcPMOEGxBYzDPbfmEkYsdMCDZ+A/D97iGWjCldi5Gu0RMA8w76M8KQQVElFEuJXmfg/zjETirxpFPV6N/lPgSd0cRSRCgLEYy4Rdhsq/fHjbyWWRgmVuKhrxtRlA6MPf2D4EUHf+scq514YRX0xPhWJMJEnXd/84AOLRA+Zpb6qOQIDAQAB";
		String rsaSignByPublicKey = rsaSignByPublicKey(str, publicKeys, "utf-8",53);
		System.out.println(rsaSignByPublicKey.replace("+", "%2B"));
		
//		byte[] keyBytes = Base64.decode(publicKeys);
//		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
//		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//		PublicKey publicKey = keyFactory.generatePublic(keySpec);
//		Cipher cipher = Cipher.getInstance("RSA");// java默认"RSA"="RSA/ECB/PKCS1Padding"
//		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//		
//		byte[] doFinal = cipher.doFinal(str.getBytes("utf-8"));
//		String replace2 = Base64.encode(doFinal).replace("+", "%2B");
//		System.out.println(replace2);
//		
//		String s ="izSpLIjpQGLuscZbKfK2gROV3iLZKBSNnDB4uQ20Ltx7xs/slDBMHgGuh1BL9fN1lSqahGhvvsJ6gffGIyVEp22BsAFCFfDt7Zle5vbwTDP2DW2zFdNw5YN3Su3LqeQTvemmjdhXWTEAUwxeEFxjflVEqc45sr8Z%2BuGx5JMqjSAWrw5KYgjAhjdgMgWHZ%2BqicrJlRnCmLPeYeZM3S0g2%2B4qvEhn1hAhUuE2pNj5ZmbJuoy1qVVCPA/E0oQcIMakbET9tnuEXSh%2BIPAenx0aQp3juKPwiD3Fawt1KVcdvLOAXy1IxfYjoV8TUvy4RpRsqXb46dq/DhKRdiaBOXnSIgA==";
//		s = rsaSignByPublicKey;
//		String replace = s.replace("%2B", "+");
//		byte[] inputByte  = Base64.decode(replace);
//		byte[] decoded =  Base64.decode("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQJkwEsAuN/LUVOcCbxOR/TGT44StKdl/4fAa+0ZdA/KETSc9JOETVVLERpCUdWyXk8i08WoFauoQMJ60PPET52Ybc5DkTT0WLSiI3ZiQ/JqOS7yZT9SU0IgWCwPpfTl3gHpW3ghyjuYbh5CDfjtI5E+RMdxZad7F9a676Mq3Upw8w4QbEFjMM9t+YSRix0wINn4D8P3uIZaMKV2Lka7REwDzDvozwpBBUSUUS4leZ+D/OMROKvGkU9Xo3+U+BJ3RxFJEKAsRjLhF2Gyr98eNvJZZGCZW4qGvG1GUDow9/YPgRQd/6xyrnXhhFfTE+FYkwkSdd3/zgA4tED5mlvqo5AgMBAAECggEAbrVK+uqt5R2pgaS6PagJCY+FmPNuSSEC8hdnItRbgyjDGMo+LfDORL1nxLG5Y4NmZiBtB4ZHBIRs99zc9++E1hLvleSdxSvDsId+IegLfIjTZfzQBfkn2pMqglJtuBa62G8E80w1bLUHV6Pab3gEEpwpYWSwwUheA4JZf4S+pqt+WHewEgY33fHW40UMPOh1rNgX6kCBD2caAbD0XPkqyAmjeRjbL9HB4hXPiR2jzsznbph5EB91Lbd/VYnEj12EZBfPiVLuv10VJ8SEVos0j2XTeIaoie3u0A2irBW/2LSED6BCLT2Ef7voLYkvBZm0hgRtZDd2ypKS0Nbrs2guAQKBgQDq7WoHVJeg9/4S0Z1q/PDvwna/iZ3DcsA2SRcCp1CsMqLymwRFB5RefQr309HNmTyWLJYld1nQQbweFMC3qTjjKtubIFtGM2xQu+VEKyyTKHYpmogvze9g53BfGJ0aRSzCBIMrdRcg7i+adD/EQNh880pXnbq9g8jropunLSJXQQKBgQCdFF9J40DQwJmAg52OjzJJcN++23B72ZvV3Fm9e6buiENTg4ns946a8FMZ0NjsNBwHPSeZRY6gQ+TBzIRxKa5KTfKCa93snXHy6bd1w3Vwxf0xKRTNzEuTOLid8ZzdF2SFNaQPWQSL2Kiqb4ibJE5vyAbBOKgrYG7xS+fWPdDM+QKBgQClgrZci8B367/Yyk05fxLQGBVJRE8dz86TcFMBz5cKenJKseQELh7dowMDLvx0+SyyMJ9nrfpcurMqDjzoKBSoFx244MbtYnVBO99+c1xoIO1+yhyAcXngr61/kfu88ldiKYOwLkIpNSXcj2INhhH/lfgeWkWiFyKKwHkhAq9bAQKBgClXsFDBKFEJMB2cBn77/29G7wzLsspWFubtzQzLG7+SS64SY93jtjvfXu/fgQiCGpJpLR/xJ73UwMOlMYKbRs01wMtE2q8pLVGS4/9xx1o8MgybVGdqCsYf2q9vZSrnukQ5PwnvEvw/tPzOyDSVFLPlAKogYFqyUeDeapF/yZH5AoGAT2tOEG4WGs+PMFasvxcJYj+EVkHsM11FvjEdl1ZxR3jcNwYs+JArgrdGif9cg+hW4VlqbTrZ6gALXLflBfZ56ghClm4Icg6pwgzmsMuoj5aBP8fSLaNEeuj796xOxgN8w+tkxjlanJxDcilykpfW8H4hrVZAk2aIxZtUAMpd/bE=");
////		
//		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
////	    //RSA解密
//	    Cipher ciphers = Cipher.getInstance("RSA");
//	    ciphers.init(Cipher.DECRYPT_MODE, priKey);
//	    String outStr = new String(ciphers.doFinal(inputByte));
//		System.out.println(outStr);
		
	}
	
	
	public static String rsaSignByPublicKey(String content, String publicKeys, String charset) {
		try {
			byte[] keyBytes = Base64.decode(publicKeys);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// java默认"RSA"="RSA/ECB/PKCS1Padding"
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] doFinal = cipher.doFinal(content.getBytes(charset));
			return Base64.encode(doFinal);
		} catch (InvalidKeySpecException ie) {
			throw new RuntimeException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", ie);
		} catch (Exception e) {
			throw new RuntimeException("RSAcontent = " + content + "; charset = " + charset, e);
		}
	}
	
	
	public static String rsaSignByPublicKey(String content, String rsaPublicKey, String charset,Integer size) {
		if(size <=0) {
			size = 53;
		}
		if(size > 53) {
			size = 53;
		}
	    String result = "";
		try {
		    // 将Base64编码后的公钥转换成PublicKey对象
			byte[] buffer = Base64.decode(rsaPublicKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			// 加密
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] inputArray = content.getBytes(charset);
			int inputLength = inputArray.length;
			// 最大加密字节数，超出最大字节数需要分组加密
			int MAX_ENCRYPT_BLOCK = size;
			// 标识
			int offSet = 0;
			byte[] resultBytes = {};
			byte[] cache = {};
			while (inputLength - offSet > 0) {
				if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
					offSet += MAX_ENCRYPT_BLOCK;
				} else {
					cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
					offSet = inputLength;
				}
				resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
				System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
			}
			result = Base64.encode(resultBytes);
		} catch (Exception e) {
			throw new RuntimeException("RSAcontent = " + content + "; charset = " + charset, e);
		}
		return result;
	}
	

}
