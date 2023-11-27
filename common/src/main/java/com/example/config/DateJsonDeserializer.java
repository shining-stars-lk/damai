package com.example.config;

import com.example.util.DateUtils;
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

public class DateJsonDeserializer extends JsonDeserializer<Date> {
	private static final List<String> formarts = new ArrayList<>(4);
	static {
		formarts.add("yyyy-MM");
		formarts.add("yyyy-MM-dd");
		formarts.add("yyyy-MM-dd HH:mm");
		formarts.add("yyyy-MM-dd HH:mm:ss");
		formarts.add("yyyy/MM/dd HH:mm:ss");
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
				return DateUtils.parse(str, formarts.get(0));
			} else if (str.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
				return DateUtils.parse(str, formarts.get(1));
			} else if (str.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
				return DateUtils.parse(str, formarts.get(2));
			} else if (str.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
				return DateUtils.parse(str, formarts.get(3));
			}  else if (str.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
				return DateUtils.parse(str, formarts.get(4));
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
		Pattern p = Pattern.compile("^[0-9]*");
		Matcher m = p.matcher(number);
		return m.matches();
	}
}
