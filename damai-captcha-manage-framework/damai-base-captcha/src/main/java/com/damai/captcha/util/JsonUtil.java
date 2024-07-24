package com.damai.captcha.util;

import com.damai.captcha.model.vo.PointVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 替换掉fastjson，自定义实现相关方法   note: 该实现不具有通用性，仅用于本项目。
 * @author: 阿星不是程序员
 **/
public class JsonUtil {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	public static List<PointVO> parseArray(String text, Class<PointVO> clazz) {
		if (text == null) {
			return null;
		} else {
			String[] arr = text.replaceFirst("\\[","")
					.replaceFirst("\\]","").split("\\}");
			List<PointVO> ret = new ArrayList<>(arr.length);
			for (String s : arr) {
				ret.add(parseObject(s,PointVO.class));
			}
			return ret;
		}
	}

	public static PointVO parseObject(String text, Class<PointVO> clazz) {
		if(text == null) {
			return null;
		}
		try {
			PointVO ret =  clazz.getDeclaredConstructor().newInstance();
			return ret.parse(text);
		}catch (Exception ex){
			logger.error("json解析异常", ex);

		}
		return null;
	}

	public static String toJsonString(Object object) {
		if(object == null) {
			return "{}";
		}
		if(object instanceof PointVO){
			PointVO t = (PointVO)object;
			return t.toJsonString();
		}
		if(object instanceof List){
			List<PointVO> list = (List<PointVO>)object;
			StringBuilder buf = new StringBuilder("[");
			list.stream().forEach(t->{
				buf.append(t.toJsonString()).append(",");
			});
			return buf.deleteCharAt(buf.lastIndexOf(",")).append("]").toString();
		}
		if(object instanceof Map){
			return ((Map)object).entrySet().toString();
		}
		throw new UnsupportedOperationException("不支持的输入类型:"
				+object.getClass().getSimpleName());
	}
}
