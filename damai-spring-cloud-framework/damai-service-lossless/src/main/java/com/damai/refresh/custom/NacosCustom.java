package com.damai.refresh.custom;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.client.naming.cache.ServiceInfoHolder;
import com.alibaba.nacos.client.naming.core.ServiceInfoUpdateService;
import com.alibaba.nacos.client.naming.remote.NamingClientProxyDelegate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: nacos缓存操作
 * @author: 阿宽不是程序员
 **/
@Slf4j
@AllArgsConstructor
public class NacosCustom {

    private static final String CLIENT_PROXY_FIELD = "clientProxy";

    private static final String SERVICE_INFO_HOLDER_FIELD = "serviceInfoHolder";

    private static final String FUTURE_MAP_FIELD = "futureMap";

    private static final String SERVICE_INFO_UPDATE_SERVICE_FIELD = "serviceInfoUpdateService";
    
    private final NacosDiscoveryProperties discoveryProperties;
    
    private final NacosAutoServiceRegistration nacosAutoServiceRegistration;
    
    private final NacosServiceManager nacosServiceManager;
    

    private Map<String,Object> getNacosCacheMap(){
        Map<String,Object> map = new HashMap<>(8);
        try {
            NamingService namingService = nacosServiceManager.getNamingService(discoveryProperties.getNacosProperties());
            if (namingService instanceof NacosNamingService) {
                NacosNamingService nacosNamingService = (NacosNamingService)namingService;
                Class<? extends NacosNamingService> nacosNamingServiceClass = nacosNamingService.getClass();
    
                Field clientProxyField = nacosNamingServiceClass.getDeclaredField(CLIENT_PROXY_FIELD);
                clientProxyField.setAccessible(true);
                NamingClientProxyDelegate namingClientProxyDelegate = (NamingClientProxyDelegate)clientProxyField.get(nacosNamingService);

                Class<? extends NamingClientProxyDelegate> namingClientProxyDelegateClass = namingClientProxyDelegate.getClass();

                Field serviceInfoHolderField = namingClientProxyDelegateClass.getDeclaredField(SERVICE_INFO_HOLDER_FIELD);
                serviceInfoHolderField.setAccessible(true);
                ServiceInfoHolder serviceInfoHolder = (ServiceInfoHolder)serviceInfoHolderField.get(namingClientProxyDelegate);
                
                Field serviceInfoUpdateServiceField = namingClientProxyDelegateClass.getDeclaredField(SERVICE_INFO_UPDATE_SERVICE_FIELD);
                serviceInfoUpdateServiceField.setAccessible(true);
                ServiceInfoUpdateService serviceInfoUpdateService =
                        (ServiceInfoUpdateService)serviceInfoUpdateServiceField.get(namingClientProxyDelegate);
                Class<? extends ServiceInfoUpdateService> serviceInfoUpdateServiceClass = serviceInfoUpdateService.getClass();
                Field futureMapField = serviceInfoUpdateServiceClass.getDeclaredField(FUTURE_MAP_FIELD);
                futureMapField.setAccessible(true);
                Map<String, ScheduledFuture<?>> futureMap = (Map<String, ScheduledFuture<?>>)futureMapField.get(serviceInfoUpdateService);

                Map<String, ServiceInfo> serviceInfoMap = serviceInfoHolder.getServiceInfoMap();
                map.put("serviceInfoMap",serviceInfoMap);
                map.put(FUTURE_MAP_FIELD,futureMap);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }
    
    public void clearNacosCache(){
        Map<String,Object> map = getNacosCacheMap();
        Map<String, ScheduledFuture<?>> futureMap = (Map<String, ScheduledFuture<?>>)map.get(FUTURE_MAP_FIELD);
        Map<String, ServiceInfo> serviceInfoMap = (Map<String, ServiceInfo>)map.get("serviceInfoMap");
        synchronized (futureMap) {
            serviceInfoMap.clear();
        }
    }
    
    public Map<String, ServiceInfo> getNacosCache(){
        Map<String,Object> map = getNacosCacheMap();
        return (Map<String, ServiceInfo>)map.get("serviceInfoMap");
    }
    
    public Boolean logoutService(){
        try {
            log.info("stop service");
            nacosAutoServiceRegistration.stop();
            log.info("nacos instance has been de-registered");
            return true;
        }catch (Exception e) {
            log.error("Logout service error ", e);
            return false;
        }
    }
}
