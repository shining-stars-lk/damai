package com.damai.balance;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.threadlocal.BaseParameterHolder;
import com.damai.util.StringUtil;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.damai.constant.Constant.GRAY_FLAG_FALSE;
import static com.damai.constant.Constant.GRAY_FLAG_TRUE;
import static com.damai.constant.Constant.GRAY_PARAMETER;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 灰度版本选择负载均衡选择器
 * @author: 阿宽不是程序员
 **/
@Slf4j
public class CustomAwarePredicate extends AbstractServerPredicate{
	
	
	private final String gray;
	
	private CustomEnabledRule customEnabledRule;
	
	private final Map<String,String> map = new HashMap<>();
	
	public CustomAwarePredicate(String gray, CustomEnabledRule customEnabledRule){
		super(customEnabledRule);
		this.gray = gray;
		this.customEnabledRule = customEnabledRule;
		this.map.put(GRAY_FLAG_FALSE, GRAY_FLAG_FALSE);
		this.map.put(GRAY_FLAG_TRUE, GRAY_FLAG_TRUE);
	}
	
	/**
	 * 灰度调用服务的说明：
	 *
	 * 请求服务中请求头中的参数 gray=false:请求生产的服务。 gray=true:请求灰度的服务
	 *
	 * 被调用服务的配置:
	 *   spring.cloud.nacos.discovery.metadata.gray=false:代表生产的服务
	 *   spring.cloud.nacos.discovery.metadata.gray=true:代表灰度的服务
	 *
	 * 如果请求服务中请求头没有gray参数，或者该参数中的值不是true或false字符串(不区分大小写)则认为gray=false
	 *
	 * 如果被调用服务的 spring.cloud.nacos.discovery.metadata.gray 配置项没有配置，或者为空，或者该配置项中的值不是true或false字符串(不区分大小写)则认为gray=false
	 * 判断逻辑:
	 *   如果所有被调用服务端的配置项 --spring.cloud.nacos.discovery.metadata.gray=true，并且请求中的Header参数 gray=true，则在该请求的n次调用中apply()函数都返回true，走负载均衡
	 *   否则被调用服务端的配置项 --spring.cloud.nacos.discovery.metadata.gray 必须与请求中的Header参数 gray 值相等 apply()函数才会返回true
	 *
	 * 总结:
	 *   生产的请求必须走生产的服务(没有部署生产服务就熔断)，灰度的请求在有灰度服务部署的情况下必须走灰度的，没有灰度服务的情况下则调用生产的服务
	 */
	
	@Override
    public boolean apply(PredicateKey input) {
		boolean result;
		try {
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			String grayFromRequest = Optional.ofNullable(requestAttributes)
					.map(ra -> (ServletRequestAttributes) ra)
					.map(sra -> sra.getRequest().getHeader(GRAY_PARAMETER))
					.filter(StringUtil::isNotEmpty)
					.orElseGet(() -> BaseParameterHolder.getParameter(GRAY_PARAMETER));
			grayFromRequest = Optional.ofNullable(grayFromRequest).filter(StringUtil::isNotEmpty).orElse(gray);
			NacosServer nacosServer = (NacosServer) input.getServer();
			String grayFromMetaData = Optional.ofNullable(nacosServer.getInstance().getMetadata())
					.filter(CollectionUtil::isNotEmpty)
					.map(metadata -> metadata.get(GRAY_PARAMETER))
					.filter(StringUtil::isNotEmpty)
					.orElse(GRAY_FLAG_FALSE);
			//判断如果被调用端没有灰度配置则默认配置为生产环境
			grayFromMetaData = Optional.ofNullable(map.get(grayFromMetaData.toLowerCase())).orElse(GRAY_FLAG_FALSE);
			//判断如果请求端没有灰度标识则默认配置为生产环境
			grayFromRequest = Optional.ofNullable(map.get(grayFromRequest.toLowerCase())).orElse(GRAY_FLAG_FALSE);
			result = grayFromMetaData.equalsIgnoreCase(grayFromRequest);
			
			/*假如最后得到的结果为false，再做一次匹配
			 *
			 * 如果所有服务端的配置均为spring.cloud.nacos.discovery.metadata.gray=true,而调用请求端的请求头中的 gray 为true，则也允许结果返回true做负载均衡
			 *
			 * 反之如果所有服务端的配置为spring.cloud.nacos.discovery.metadata.gray=true,而调用请求端的请求头中的 gray 为false，则结果返回false,不允许做负载均衡
			 */
			if (!result && grayFromRequest.equalsIgnoreCase(GRAY_FLAG_TRUE)) {
				List<Server> servers = Optional.ofNullable(customEnabledRule)
						.map(CustomEnabledRule::getLoadBalancer)
						.map(ILoadBalancer::getReachableServers)
						.filter(CollectionUtil::isNotEmpty)
						.orElseThrow(() -> new DaMaiFrameException(BaseCode.SERVER_LIST_NOT_EXIST));
				Map<String,String> map = new HashMap<>(servers.size());
				for (Server server : servers) {
					NacosServer nacosServerInstance = (NacosServer) server;
					String balanceGray = nacosServerInstance.getInstance().getMetadata().get(GRAY_PARAMETER);
					//判断如果被调用端没有灰度配置则默认配置为生产环境
					if (StringUtil.isEmpty(balanceGray) || Objects.isNull(map.get(balanceGray.toLowerCase()))) {
						balanceGray = GRAY_FLAG_FALSE;
					}
					map.put(balanceGray,balanceGray);
				}
				if(Objects.isNull(map.get(GRAY_FLAG_TRUE))) {
					result = true;
				}
			}
		}catch (Exception e) {
			result = false;
			log.error("CustomAwarePredicate#apply error",e);
		} return result;
	}
}
