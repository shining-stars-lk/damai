//package com.damai.conf.swagger;
//
//
//import cn.hutool.core.collection.CollUtil;
//import com.damai.util.StringUtil;
//import lombok.AllArgsConstructor;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
//import org.springframework.cloud.gateway.config.GatewayProperties;
//import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
//import org.springframework.cloud.gateway.route.RouteDefinition;
//import springfox.documentation.swagger.web.SwaggerResource;
//import springfox.documentation.swagger.web.SwaggerResourcesProvider;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Optional;
//
//
///**
// * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
// * @description: swagger 信息
// * @author: 阿星不是程序员
// **/
//@AllArgsConstructor
//public class SwaggerProvider implements SwaggerResourcesProvider {
//    
//    private final DiscoveryClient discoveryClient;
//    
//    private final LoadBalancerClient loadBalancerClient;
//    
//    private final GatewayProperties gatewayProperties;
//    
//    @Override
//    public List<SwaggerResource> get() {
//        List<SwaggerResource> resources = new ArrayList<>();
//        Map<String, Map<String, String>> serviceMap = getServiceMappingNames(gatewayProperties);
//        List<String> servicesNeedIgnoring = new ArrayList<>();
//        servicesNeedIgnoring.add("*");
//        discoveryClient.getServices().stream().filter(serviceId ->
//                !servicesNeedIgnoring.contains(serviceId)
//        ).forEach(serviceId -> {
//            ServiceInstance selectedInstance = loadBalancerClient.choose(serviceId);
//            if (selectedInstance != null) {
//                Map<String, String> serviceMapping = serviceMap.get(serviceId);
//                if(serviceMapping!=null) {
//                    String serviceName = serviceMapping.get("title");
//                    String pathName = serviceMapping.get("path").replace("**", "");
//                    if (StringUtil.isEmpty(serviceName)) {
//                        serviceName = serviceId;
//                    }
//                    resources.add(swaggerResource(serviceName+"—"+pathName, pathName+"v2/api-docs?group=default","1.0" ));
//                }
//            }
//        });
//        return resources;
//    }
//    
//    private Map<String, Map<String, String>> getServiceMappingNames(GatewayProperties gatewayProperties) {
//        Map<String, Map<String, String>> serviceMap = new HashMap<>(256);
//        List<RouteDefinition> routeList = gatewayProperties.getRoutes();
//        if (CollUtil.isNotEmpty(routeList)) {
//            for (RouteDefinition route : routeList) {
//                Map<String,String> map = new HashMap<>(4);
//                String serviceId = Optional.ofNullable(route.getMetadata().get("relServiceName"))
//                        .map(String::valueOf).filter(StringUtil::isNotEmpty).orElse(route.getId());
//                map.put("serviceId",serviceId);
//                Map<String, Object> metadata = route.getMetadata();
//                String title = null;
//                if (metadata != null) {
//                    Object titleObject = metadata.get("title");
//                    if (titleObject != null || titleObject instanceof String) {
//                        title = (String)titleObject;
//                    }
//                }
//                if (StringUtil.isNotEmpty(title)) {
//                    map.put("title",title);
//                }
//                List<PredicateDefinition> predicateList = route.getPredicates();
//                if (CollUtil.isNotEmpty(predicateList)) {
//                    PredicateDefinition predicateDefinition = predicateList.get(0);
//                    if (predicateDefinition != null) {
//                        Map<String, String> argMap = predicateDefinition.getArgs();
//                        if (CollUtil.isNotEmpty(argMap)) {
//                            for (final Entry<String, String> entry : argMap.entrySet()) {
//                                map.put("path",entry.getValue());
//                            }
//                        }
//                    }
//                }
//                serviceMap.put(serviceId,map);
//            }
//        }
//        return serviceMap;
//    }
//    
//    private SwaggerResource swaggerResource(String name, String location, String version) {
//        SwaggerResource swaggerResource = new SwaggerResource();
//        swaggerResource.setName(name);
//        swaggerResource.setLocation(location);
//        swaggerResource.setSwaggerVersion(version);
//        return swaggerResource;
//    }
//}
