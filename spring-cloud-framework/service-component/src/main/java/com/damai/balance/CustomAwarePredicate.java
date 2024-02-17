package com.damai.balance;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.threadlocal.BaseParameterHolder;
import com.google.common.collect.Maps;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.damai.constant.Constant.MARK_FLAG_FALSE;
import static com.damai.constant.Constant.MARK_FLAG_TRUE;
import static com.damai.constant.Constant.MARK_PARAMETER;

/**
 * @program: cook-frame
 * @description: 灰度版本选择负载均衡选择器
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Slf4j
public class CustomAwarePredicate extends AbstractServerPredicate{
	
	
	private String mark;
	
	private CustomEnabledRule customEnabledRule;
	
	public CustomAwarePredicate(String mark, CustomEnabledRule customEnabledRule){
		super(customEnabledRule);
		this.mark = mark;
		this.customEnabledRule = customEnabledRule;
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
			if (metadata != null || metadata.isEmpty() == true) {
				markFromMetaData = MARK_FLAG_FALSE;
			}else {
				markFromMetaData = metadata.get(MARK_PARAMETER);
			}
			
			if(markFromMetaData == null || !(markFromMetaData.equalsIgnoreCase(MARK_FLAG_FALSE) || markFromMetaData.equalsIgnoreCase(MARK_FLAG_TRUE))) {
				markFromMetaData = MARK_FLAG_FALSE;
			}
			
			if(markFromRequest == null || !(markFromRequest.equalsIgnoreCase(MARK_FLAG_FALSE) || markFromRequest.equalsIgnoreCase(MARK_FLAG_FALSE))) {
				markFromRequest = MARK_FLAG_FALSE;
			}
			if(markFromMetaData.equalsIgnoreCase(markFromRequest)) {
				result = true;
			}else {
				result = false;
			}
			
			if(result == false && markFromRequest.equalsIgnoreCase(MARK_FLAG_TRUE)) {
				if(customEnabledRule == null) {
					throw new DaMaiFrameException(BaseCode.CUSTOM_ENABLED_RULE_EMPTY);
				}
				ILoadBalancer iLoadBalancer = customEnabledRule.getLoadBalancer();
				if(iLoadBalancer == null) {
					throw new DaMaiFrameException(BaseCode.I_LOAD_BALANCER_RULE_EMPTY);
				}
				List<Server> serverList = iLoadBalancer.getReachableServers();
				if(CollUtil.isEmpty(serverList)) {
					throw new DaMaiFrameException(BaseCode.SERVER_LIST_EMPTY);
				}
				Map<String,String> map = Maps.newHashMap();
				for (Server serverBalance : serverList) {
					NacosServer server = (NacosServer) serverBalance;
					String markFromBalance = server.getInstance().getMetadata().get(MARK_PARAMETER);
					
					if(markFromBalance == null || !(markFromBalance.equalsIgnoreCase(MARK_FLAG_FALSE) || markFromBalance.equalsIgnoreCase(MARK_FLAG_TRUE))) {
						markFromBalance = MARK_FLAG_FALSE;
					}
					map.put(markFromBalance,markFromBalance);
				}
				if(!map.containsKey(MARK_FLAG_TRUE)) {
					result = true;
				}
			}
		}catch (Exception e) {
			result = false;
			log.error("CustomAwarePredicate#apply error",e);
		}
		return result;
	}
}
