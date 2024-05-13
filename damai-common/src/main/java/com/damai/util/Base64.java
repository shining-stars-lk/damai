package com.damai.util;

import java.util.Arrays;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: base64
 * @author: 阿星不是程序员
 **/
public final class Base64 {

	private static final int BASE_LENGTH = 128;
	private static final int LOOKUP_LENGTH = 64;
	private static final int TWENTY_FOUR_BIT_GROUP = 24;
	private static final int EIGHT_BIT = 8;
	private static final int SIXTEEN_BIT = 16;
	private static final int FOUR_BYTE = 4;
	private static final int SIGN = -128;
	private static final char PAD = '=';
	private static final byte[] BASE64_ALPHABET = new byte[BASE_LENGTH];
	private static final char[] LOOKUP_BASE64_ALPHABET = new char[LOOKUP_LENGTH];
	
	private static final char A_UPPER_CASE = 'A';
	private static final char A_LOWER_CASE = 'a';
	private static final char Z_UPPER_CASE = 'Z';
	
	private static final char Z_LOWER_CASE = 'z';
	
	private static final char ZERO = '0';
	
	private static final char NINE = '9';
	
	private static final int TWENTY_FIVE = 25;
	
	private static final int TWENTY_SIX = 26;
	
	private static final int FIFTY_ONE = 51;
	
	private static final int FIFTY_TWO = 52;
	
	private static final int SIXTY_ONE = 61;
	
	private static final byte LOW_FOUR_BITS_MASK = 0xf;
	
	private static final byte LOW_TWO_BITS_MASK = 0x3;

	static {
		Arrays.fill(BASE64_ALPHABET, (byte) -1);
		for (int i = Z_UPPER_CASE; i >= A_UPPER_CASE; i--) {
			BASE64_ALPHABET[i] = (byte) (i - A_UPPER_CASE);
		}
		for (int i = Z_LOWER_CASE; i >= A_LOWER_CASE; i--) {
			BASE64_ALPHABET[i] = (byte) (i - A_LOWER_CASE + 26);
		}

		for (int i = NINE; i >= ZERO; i--) {
			BASE64_ALPHABET[i] = (byte) (i - ZERO + 52);
		}

		BASE64_ALPHABET['+'] = 62;
		BASE64_ALPHABET['/'] = 63;

		for (int i = 0; i <= TWENTY_FIVE; i++) {
			LOOKUP_BASE64_ALPHABET[i] = (char) (A_UPPER_CASE + i);
		}

		for (int i = TWENTY_SIX, j = 0; i <= FIFTY_ONE; i++, j++) {
			LOOKUP_BASE64_ALPHABET[i] = (char) (A_LOWER_CASE + j);
		}

		for (int i = FIFTY_TWO, j = 0; i <= SIXTY_ONE; i++, j++) {
			LOOKUP_BASE64_ALPHABET[i] = (char) (ZERO + j);
		}
		LOOKUP_BASE64_ALPHABET[62] = '+';
		LOOKUP_BASE64_ALPHABET[63] = '/';

	}

	private static boolean isWhiteSpace(char octect) {
		return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
	}

	private static boolean isPad(char octect) {
		return (octect == PAD);
	}

	private static boolean isData(char octect) {
		return (octect < BASE_LENGTH && BASE64_ALPHABET[octect] != -1);
	}

	/**
	 * Encodes hex octects into Base64
	 * 
	 * @param binaryData
	 *            Array containing binaryData
	 * @return Encoded Base64 array
	 */
	public static String encode(byte[] binaryData) {

		if (binaryData == null) {
			return null;
		}

		int lengthDataBits = binaryData.length * EIGHT_BIT;
		if (lengthDataBits == 0) {
			return "";
		}

		int fewerThan24bits = lengthDataBits % TWENTY_FOUR_BIT_GROUP;
		int numberTriplets = lengthDataBits / TWENTY_FOUR_BIT_GROUP;
		int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1
				: numberTriplets;
		char[] encodedData = new char[numberQuartet * 4];

		byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

		int encodedIndex = 0;
		int dataIndex = 0;

		for (int i = 0; i < numberTriplets; i++) {
			b1 = binaryData[dataIndex++];
			b2 = binaryData[dataIndex++];
			b3 = binaryData[dataIndex++];

			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);
			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
					: (byte) ((b2) >> 4 ^ 0xf0);
			byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6)
					: (byte) ((b3) >> 6 ^ 0xfc);

			encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[val1];
			encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[val2 | (k << 4)];
			encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[(l << 2) | val3];
			encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[b3 & 0x3f];
		}

		// form integral number of 6-bit groups
		if (fewerThan24bits == EIGHT_BIT) {
			b1 = binaryData[dataIndex];
			k = (byte) (b1 & 0x03);
			
			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);
			encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[val1];
			encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[k << 4];
			encodedData[encodedIndex++] = PAD;
			encodedData[encodedIndex++] = PAD;
		} else if (fewerThan24bits == SIXTEEN_BIT) {
			b1 = binaryData[dataIndex];
			b2 = binaryData[dataIndex + 1];
			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);
			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
					: (byte) ((b2) >> 4 ^ 0xf0);

			encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[val1];
			encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[val2 | (k << 4)];
			encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[l << 2];
			encodedData[encodedIndex++] = PAD;
		}

		return new String(encodedData);
	}

	/**
	 * remove WhiteSpace from MIME containing encoded Base64 data.
	 * 
	 * @param data
	 *            the byte array of base64 data (with WS)
	 * @return the new length
	 */
	private static int removeWhiteSpace(char[] data) {
		if (data == null) {
			return 0;
		}

		// count characters that's not whitespace
		int newSize = 0;
		int len = data.length;
		for (int i = 0; i < len; i++) {
			if (!isWhiteSpace(data[i])) {
				data[newSize++] = data[i];
			}
		}
		return newSize;
	}
}
