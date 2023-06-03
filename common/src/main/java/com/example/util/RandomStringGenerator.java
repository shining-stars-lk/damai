package com.example.util;


import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;

/**
 * @ClassName: RandomStringGenerator
 * @Description: 随机字符生成类
 * 
 */
public class RandomStringGenerator {

	public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * @Title: gen8DigitalString
	 * @Description: 生成8位的随机字符串(含大小写字母、数字)
	 * @return String 返回8位的随机字符串
	 * @throws
	 */
	public static String gen8DigitalString() {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		int divider = ALLCHAR.length();
		for (int i = 0; i < 8; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(divider)));
		}
		return sb.toString();
	}
	
	/**
	 * 使用apache的common包自动生成随机数
	 * @param length 生成的字符串的长度
	 * @return	数字字符串
	 */
	public static String genRandomNumeric(int length) {
		return RandomStringUtils.randomNumeric(length);
	}
	
	/**
	 * 生成随机的字符串可以指定长度
	 * @param n
	 * @return
	 */
	public static String getRandomID(int n){
        String val = "";
        Random random = new Random();
        for ( int i = 0; i < n; i++ )
        {
            String str = random.nextInt( 2 ) % 2 == 0 ? "num" : "char";
            if ( "char".equalsIgnoreCase( str ) )
            { // 产生字母
                int nextInt = random.nextInt( 2 ) % 2 == 0 ? 65 : 97;
                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                val += (char) ( nextInt + random.nextInt( 26 ) );
            }
            else if ( "num".equalsIgnoreCase( str ) )
            { // 产生数字
                val += String.valueOf( random.nextInt( 10 ) );
            }
        }
        return val;
    }
	
	/**
	 * 使用apache的common包自动生成随机数
	 * @return	数字字符串
	 */
	public static String genRandomNumeric(int min,int max) {
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return String.valueOf(s);
	}
	public static void main(String[] args) {
		System.out.println(genRandomNumeric(100000, 999999));
		System.out.println(genRandomNumeric(6));
		System.out.println(gen8DigitalString());
		System.out.println(getRandomID(6));
	}
}
