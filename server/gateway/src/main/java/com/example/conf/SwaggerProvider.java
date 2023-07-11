package com.example.conf;


import com.example.core.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Primary
@Component
public class SwaggerProvider implements SwaggerResourcesProvider {
    
    @Value("${api.path:patientapi}")
    private String apiPath;
    
    @Autowired
    DiscoveryClient discoveryClient;
    
    @Autowired
    LoadBalancerClient loadBalancerClient;
    
    @Autowired
    GatewayProperties gatewayProperties;
    
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        Map<String, Map<String, String>> serviceMappingNames = getServiceMappingNames(gatewayProperties);
        List<String> servicesNeedIgnoring = new ArrayList<>();
        servicesNeedIgnoring.add("*");
        //Add the registered micro-services swagger docs as additional swagger resources
        discoveryClient.getServices().stream().filter(serviceId ->
                !servicesNeedIgnoring.contains(serviceId)
        ).forEach(serviceId -> {
            ServiceInstance selectedInstance = loadBalancerClient.choose(serviceId);
            if (selectedInstance != null) {
                Map<String, String> serviceMapping = serviceMappingNames.get(serviceId);
                if(serviceMapping!=null) {
                    String serviceName = serviceMapping.get("title");
                    String pathName = serviceMapping.get("path").replace("**", "");
                    if (StringUtil.isEmpty(serviceName)) {
                        serviceName = serviceId;
                    }
                    resources.add(swaggerResource(serviceName+"—"+pathName, pathName+"v2/api-docs?group=default","1.0" ));
                }
                
            }
        });
        return resources;
    }
    
    private Map<String, Map<String, String>> getServiceMappingNames(GatewayProperties gatewayProperties) {
        Map<String, Map<String, String>> serviceMappingNames = new HashMap<>();
        List<RouteDefinition> routes = gatewayProperties.getRoutes();
        if (routes != null && routes.size() > 0) {
            for (RouteDefinition route : routes) {
                Map<String,String> map = new HashMap<>(4);
                map.put("serviceId",route.getId());
                Map<String, Object> metadata = route.getMetadata();
                String title = null;
                if (metadata != null) {
                    Object object = metadata.get("title");
                    if (object != null || object instanceof String) {
                        title = (String)object;
                    }
                }
                if (StringUtil.isNotEmpty(title)) {
                    map.put("title",title);
                }
                List<PredicateDefinition> predicates = route.getPredicates();
                if (predicates != null && predicates.size() > 0) {
                    PredicateDefinition predicateDefinition = predicates.get(0);
                    if (predicateDefinition != null) {
                        Map<String, String> args = predicateDefinition.getArgs();
                        if (args != null && args.size() > 0) {
                            for (final Entry<String, String> entry : args.entrySet()) {
                                map.put("path",entry.getValue());
                            }
                        }
                    }
                }
                serviceMappingNames.put(route.getId(),map);
            }
        }
        return serviceMappingNames;
    }
    
    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
