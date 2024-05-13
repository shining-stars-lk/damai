package com.damai.config;

import com.damai.util.DateUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 自定义json序列化
 * @author: 阿星不是程序员
 **/
public class DateJsonDeserializer extends JsonDeserializer<Date> {
	
	private static final Pattern P = Pattern.compile("^[0-9]*");
	private static final List<String> FORMAT = new ArrayList<>(4);
	static {
		FORMAT.add("yyyy-MM");
		FORMAT.add("yyyy-MM-dd");
		FORMAT.add("yyyy-MM-dd HH:mm");
		FORMAT.add("yyyy-MM-dd HH:mm:ss");
		FORMAT.add("yyyy/MM/dd HH:mm:ss");
	}

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Date convertDate = null;
		String str = p.getText();
		
		if(str == null || "".equals(str)) {
			return null;
		}
		
//		校验字符串是否是数字 
		if(isNum(str)) {
			convertDate = DateUtils.parse(Long.valueOf(str));
		}else {
			if (str.matches("^\\d{4}-\\d{1,2}$")) {
				return DateUtils.parse(str, FORMAT.get(0));
			} else if (str.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
				return DateUtils.parse(str, FORMAT.get(1));
			} else if (str.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
				return DateUtils.parse(str, FORMAT.get(2));
			} else if (str.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
				return DateUtils.parse(str, FORMAT.get(3));
			}  else if (str.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
				return DateUtils.parse(str, FORMAT.get(4));
			}else {
				throw new IllegalArgumentException("Invalid boolean value '" + str + "'");
			}
		}
		
		return convertDate;
	}
	
	
	/**
	 * 校验字符串是否是数字
	 *
	 * @param number
	 * @return
	 */
	public static boolean isNum(String number) {
		Matcher m = P.matcher(number);
		return m.matches();
	}
}
