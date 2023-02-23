package com.nacosrefresh.handle;


import com.example.core.SpringUtil;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerListUpdater;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: ribbon缓存管理
 * @author: lk
 * @create: 2022-06-01
 **/

public class RibbonHandle {

    private static final String CONTEXTS_FIELD = "contexts";

    private static final String RIBBON_LOAD_BALANCER = "ribbonLoadBalancer";

    private static final String UPDATE_ACTION_FIELD = "updateAction";

    private static final String ALL_SERVER_LIST_FIELD = "allServerList";

    private static final String UP_SERVER_LIST_FIELD = "upServerList";

    private Map<String,ZoneAwareLoadBalancer> zoneAwareLoadBalancerMap(){
        Map<String,ZoneAwareLoadBalancer> zoneAwareLoadBalancerMap = new HashMap<>();
        try{
            //该类是 Spring 创建 Ribbon 客户端、负载均衡器、客户端配置实例的工厂，并且为每个 client name 创建对应的 Spring ApplicationContext。
            SpringClientFactory springClientFactory = SpringUtil.getBean(SpringClientFactory.class);
            if (springClientFactory != null) {
                Class<? extends SpringClientFactory> aClass = springClientFactory.getClass();
                Field contextsField = aClass.getSuperclass().getDeclaredField(CONTEXTS_FIELD);
                contextsField.setAccessible(true);
                //1,反射获得到contexts，此属性为Map<String, AnnotationConfigApplicationContext>，
                //2,Key为每个fegin的服务名，Value为每个服务自己的spring容器(此容器有个父容器也就是此项目中的spring容器)
                Map<String, AnnotationConfigApplicationContext> map = (Map<String, AnnotationConfigApplicationContext>)contextsField.get(springClientFactory);
                //每个fegin服务的zoneAwareLoadBalancer
                for (Map.Entry<String, AnnotationConfigApplicationContext> entry : map.entrySet()) {
                    String serverName = entry.getKey();
                    AnnotationConfigApplicationContext context = entry.getValue();
                    ILoadBalancer iLoadBalancer = context.getBean(RIBBON_LOAD_BALANCER, ILoadBalancer.class);
                    if (iLoadBalancer instanceof ZoneAwareLoadBalancer) {
                        //ZoneAwareLoadBalancer为负载均衡器
                        ZoneAwareLoadBalancer zoneAwareLoadBalancer = (ZoneAwareLoadBalancer)iLoadBalancer;
                        zoneAwareLoadBalancerMap.put(serverName,zoneAwareLoadBalancer);
                    }
                }
            }
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
        return zoneAwareLoadBalancerMap;
    }

    /**
     * 更新ribbon缓存(此操作会调取nacos服务，nacos里面的逻辑又会把服务放进nacos自己的缓存中)
     * */
    public void updateRibbonCache(){
        try {
            Map<String, ZoneAwareLoadBalancer> zoneAwareLoadBalancerMap = zoneAwareLoadBalancerMap();
            //循环更新每个fegin服务的ribbon服务缓存
            for (Map.Entry<String, ZoneAwareLoadBalancer> zoneAwareLoadBalancerEntry : zoneAwareLoadBalancerMap.entrySet()) {
                ZoneAwareLoadBalancer zoneAwareLoadBalancer = zoneAwareLoadBalancerEntry.getValue();
                Class<? extends ZoneAwareLoadBalancer> aClass1 = zoneAwareLoadBalancer.getClass();
                Field updateActionField = aClass1.getSuperclass().getDeclaredField(UPDATE_ACTION_FIELD);
                updateActionField.setAccessible(true);
                //1,反射拿到ZoneAwareLoadBalancer的父类DynamicServerListLoadBalancer中updateAction属性
                //2,updateAction为接口，调用doUpDate()方法实际调用updateListOfServers()
                //3,updateListOfServers()就是调用nacos服务并更新到ribbon自己的缓存中
                ServerListUpdater.UpdateAction updateAction = (ServerListUpdater.UpdateAction)updateActionField.get(zoneAwareLoadBalancer);
                //ribbon更新自己缓存的逻辑中，
                //用的BaseLoadBalancer中的ReadWriteLock allServerLock = new ReentrantReadWriteLock()
                //和ReadWriteLock upServerLock = new ReentrantReadWriteLock()
                //保证了线程安全所以不用重写线程安全的逻辑
                updateAction.doUpdate();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public Map getRibbonCache(){
        Map taotalMap = new HashMap();
        try {
            Map<String, ZoneAwareLoadBalancer> zoneAwareLoadBalancerMap = zoneAwareLoadBalancerMap();
            for (Map.Entry<String, ZoneAwareLoadBalancer> zoneAwareLoadBalancerEntry : zoneAwareLoadBalancerMap.entrySet()) {
                String serverName = zoneAwareLoadBalancerEntry.getKey();
                ZoneAwareLoadBalancer zoneAwareLoadBalancer = zoneAwareLoadBalancerEntry.getValue();
                Class<? extends ZoneAwareLoadBalancer> aClass1 = zoneAwareLoadBalancer.getClass();
                Field allServerListField = aClass1.getSuperclass().getSuperclass().getDeclaredField(ALL_SERVER_LIST_FIELD);
                allServerListField.setAccessible(true);
                List<Server> allServerList = (List<Server>)allServerListField.get(zoneAwareLoadBalancer);
                Field upServerListField = aClass1.getSuperclass().getSuperclass().getDeclaredField(UP_SERVER_LIST_FIELD);
                upServerListField.setAccessible(true);
                List<Server> upServerList = (List<Server>)upServerListField.get(zoneAwareLoadBalancer);
                Map mapServerList = new HashMap();
                mapServerList.put(ALL_SERVER_LIST_FIELD,allServerList);
                mapServerList.put(UP_SERVER_LIST_FIELD,upServerList);
                taotalMap.put(serverName,mapServerList);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return taotalMap;
    }
}
