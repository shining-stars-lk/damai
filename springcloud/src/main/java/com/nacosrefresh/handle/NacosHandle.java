package com.nacosrefresh.handle;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.client.naming.cache.ServiceInfoHolder;
import com.alibaba.nacos.client.naming.core.ServiceInfoUpdateService;
import com.alibaba.nacos.client.naming.remote.NamingClientProxyDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @program: bjgoodwill-msa-scloud
 * @description: nacos缓存管理
 * @author: lk
 * @create: 2022-06-01
 **/
public class NacosHandle implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(NacosHandle.class);

    private static final String CLIENT_PROXY_FIELD = "clientProxy";

    private static final String SERVICE_INFO_HOLDER_FIELD = "serviceInfoHolder";

    private static final String FUTURE_MAP_FIELD = "futureMap";

    private static final String SERVICE_INFO_UPDATE_SERVICE_FIELD = "serviceInfoUpdateService";

    /**
     * nacos配置项
     *
     * */
    private NacosDiscoveryProperties discoveryProperties;

    /**
     * nacos服务操作管理
     * */
    private NacosAutoServiceRegistration nacosAutoServiceRegistration;

    private ApplicationContext applicationContext;

    public NacosHandle(NacosDiscoveryProperties discoveryProperties,NacosAutoServiceRegistration nacosAutoServiceRegistration){
        this.discoveryProperties = discoveryProperties;
        this.nacosAutoServiceRegistration = nacosAutoServiceRegistration;
    }

    private Map nacosCache(){
        Map map = new HashMap();
        try {
            //获得nacos的操作约定接口NamingService
            NamingService namingService = discoveryProperties.namingServiceInstance();
            //NamingService的唯一实现类NacosNamingService
            if (namingService instanceof NacosNamingService) {
                NacosNamingService nacosNamingService = (NacosNamingService)namingService;
                Class<? extends NacosNamingService> aClass1 = nacosNamingService.getClass();
                //反射拿到clientProxy属性，此属性是调取服务作用分为三种NamingClientProxyDelegate、NamingGrpcClientProxy、NamingHttpClientProxy
                //这里为NamingClientProxyDelegate
                Field clientProxyField = aClass1.getDeclaredField(CLIENT_PROXY_FIELD);
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
        Map map = nacosCache();
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
        Map map = nacosCache();
        return (Map<String, ServiceInfo>)map.get("serviceInfoMap");
    }

    /**
     * 停止服务
     * */
    public Boolean stopService(){
        try {
            logger.info("Ready to stop service");
            nacosAutoServiceRegistration.stop();
            logger.info("Nacos instance has been de-registered");
            return true;
        }catch (Exception e) {
            logger.error("stopService error ", e);
            return false;
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
