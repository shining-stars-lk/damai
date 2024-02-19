package com.damai.balance;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.damai.threadlocal.BaseParameterHolder;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.damai.constant.Constant.MARK_FLAG_FALSE;
import static com.damai.constant.Constant.MARK_FLAG_TRUE;
import static com.damai.constant.Constant.MARK_PARAMETER;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 灰度版本选择负载均衡选择器
 * @author: 阿宽不是程序员
 **/
@Slf4j
public class CustomAwarePredicate extends AbstractServerPredicate{
	
	
	private final String mark;
	
	private CustomEnabledRule customEnabledRule;
	
	private final Map<String,String> map = new HashMap<>();
	
	public CustomAwarePredicate(String mark, CustomEnabledRule customEnabledRule){
		super(customEnabledRule);
		this.mark = mark;
		this.customEnabledRule = customEnabledRule;
		this.map.put(MARK_FLAG_FALSE,MARK_FLAG_FALSE);
		this.map.put(MARK_FLAG_TRUE,MARK_FLAG_TRUE);
	}
	
	@Override
    public boolean apply(PredicateKey input) {
		boolean result;
		try {
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			String markFromRequest = null;
			if (ra != null) {
				ServletRequestAttributes sra = (ServletRequestAttributes) ra;
				HttpServletRequest request = sra.getRequest();
				markFromRequest = request.getHeader(MARK_PARAMETER);
			}else {
				markFromRequest = BaseParameterHolder.getParameter(MARK_PARAMETER);
			}
			if (StringUtils.isEmpty(markFromRequest)) {
				markFromRequest = mark;
			}
			NacosServer nacosServer = (NacosServer) input.getServer();
			Map<String, String> metadata = nacosServer.getInstance().getMetadata();
			String markFromMetaData;
			if (CollectionUtil.isEmpty(metadata)) {
				markFromMetaData = MARK_FLAG_FALSE;
			}else {
				markFromMetaData = metadata.get(MARK_PARAMETER);
			}
			
			if(Objects.isNull(markFromMetaData) || Objects.isNull(map.get(markFromMetaData.toLowerCase()))) {
				markFromMetaData = MARK_FLAG_FALSE;
			}
			
			if(Objects.isNull(markFromRequest) || Objects.isNull(map.get(markFromRequest.toLowerCase()))) {
				markFromRequest = MARK_FLAG_FALSE;
			}
			result = markFromMetaData.equalsIgnoreCase(markFromRequest);
			
		}catch (Exception e) {
			result = false;
			log.error("CustomAwarePredicate#apply error",e);
		}
		return result;
	}
}
