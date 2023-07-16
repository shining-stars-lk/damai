package com.example.refresh.custom;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
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
 * @description: nacos缓存管理
 * @author: 星哥
 * @create: 2023-06-01
 **/
@Slf4j
@AllArgsConstructor
public class NacosCustom {

    private static final String CLIENT_PROXY_FIELD = "clientProxy";

    private static final String SERVICE_INFO_HOLDER_FIELD = "serviceInfoHolder";

    private static final String FUTURE_MAP_FIELD = "futureMap";

    private static final String SERVICE_INFO_UPDATE_SERVICE_FIELD = "serviceInfoUpdateService";

    /**
     * nacos配置项
     *
     * */
    private final NacosDiscoveryProperties discoveryProperties;

    /**
     * nacos服务操作管理
     * */
    private final NacosAutoServiceRegistration nacosAutoServiceRegistration;
    

    private Map getNacosCacheMap(){
        Map map = new HashMap();
        try {
            //获得nacos的操作约定接口NamingService
            NamingService namingService = discoveryProperties.namingServiceInstance();
            //NamingService的唯一实现类NacosNamingService
            if (namingService instanceof NacosNamingService) {
                NacosNamingService nacosNamingService = (NacosNamingService)namingService;
                Class<? extends NacosNamingService> nacosNamingServiceClass = nacosNamingService.getClass();
                //反射拿到clientProxy属性，此属性是调取服务作用分为三种NamingClientProxyDelegate、NamingGrpcClientProxy、NamingHttpClientProxy
                //这里为NamingClientProxyDelegate
                Field clientProxyField = nacosNamingServiceClass.getDeclaredField(CLIENT_PROXY_FIELD);
                clientProxyField.setAccessible(true);
                NamingClientProxyDelegate namingClientProxyDelegate = (NamingClientProxyDelegate)clientProxyField.get(nacosNamingService);

                Class<? extends NamingClientProxyDelegate> namingClientProxyDelegateClass = namingClientProxyDelegate.getClass();
                //1,由于nacos是通过NamingClientProxyDelegate通过subscribe实现订阅里面通过定时任务拉取nacos服务缓存到serviceInfoHolder中
                //2,所以这里要通过反射拿到serviceInfoHolder，将里面的缓存清除，
                //3,这样当ribbon更新自己的缓存时会拉取nacos服务，这时nacos已经没有缓存就会重新调用grpc真正从nacos注册中心拉取最新的服务，再存入serviceInfoHolder中。
                Field serviceInfoHolderField = namingClientProxyDelegateClass.getDeclaredField(SERVICE_INFO_HOLDER_FIELD);
                serviceInfoHolderField.setAccessible(true);
                ServiceInfoHolder serviceInfoHolder = (ServiceInfoHolder)serviceInfoHolderField.get(namingClientProxyDelegate);

                //反射拿到serviceInfoUpdateService属性
                Field serviceInfoUpdateServiceField = namingClientProxyDelegateClass.getDeclaredField(SERVICE_INFO_UPDATE_SERVICE_FIELD);
                serviceInfoUpdateServiceField.setAccessible(true);
                ServiceInfoUpdateService serviceInfoUpdateService =
                        (ServiceInfoUpdateService)serviceInfoUpdateServiceField.get(namingClientProxyDelegate);
                //反射拿到serviceInfoMap属性
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

    /**
     * 将nacos缓存清空
     * */
    public void clearNacosCache(){
        Map map = getNacosCacheMap();
        Map<String, ScheduledFuture<?>> futureMap = (Map<String, ScheduledFuture<?>>)map.get(FUTURE_MAP_FIELD);
        Map<String, ServiceInfo> serviceInfoMap = (Map<String, ServiceInfo>)map.get("serviceInfoMap");
        //这里加锁是为nacos本身的定时任务做线程安全，定时任务中的锁也是用的synchronized (futureMap)
        synchronized (futureMap) {
            serviceInfoMap.clear();
        }
    }

    /**
     * 获得nacos缓存
     * */
    public Map getNacosCache(){
        Map map = getNacosCacheMap();
        return (Map<String, ServiceInfo>)map.get("serviceInfoMap");
    }

    /**
     * 注销服务
     * */
    public Boolean LogoutService(){
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
