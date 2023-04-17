package com.extra.ribbon;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

/**
 * @program: 负载均衡选择器，只使用服务中含有A区域的服务
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Log4j2
public class MetadataAwarePredicate extends AbstractServerPredicate{

	@Override
    public boolean apply(PredicateKey input) {
		NacosServer server = (NacosServer) input.getServer();
		Map<String, String> metadata = server.getInstance().getMetadata();
		if (metadata != null) {
			String region = metadata.get("region");
			log.info("region: {}",region);	
		}
		return true;
	}
}
