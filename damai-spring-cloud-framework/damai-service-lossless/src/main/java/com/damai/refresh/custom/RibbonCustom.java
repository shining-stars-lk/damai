package com.damai.refresh.custom;


import com.damai.core.SpringUtil;
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
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: ribbon缓存操作
 * @author: 阿宽不是程序员
 **/

public class RibbonCustom {

    private static final String CONTEXTS_FIELD = "contexts";

    private static final String RIBBON_LOAD_BALANCER = "ribbonLoadBalancer";

    private static final String UPDATE_ACTION_FIELD = "updateAction";

    private static final String ALL_SERVER_LIST_FIELD = "allServerList";

    private static final String UP_SERVER_LIST_FIELD = "upServerList";

    private Map<String,ZoneAwareLoadBalancer> zoneAwareLoadBalancerMap(){
        Map<String,ZoneAwareLoadBalancer> zoneAwareLoadBalancerMap = new HashMap<>(256);
        try{
            SpringClientFactory springClientFactory = SpringUtil.getBean(SpringClientFactory.class);
            if (springClientFactory != null) {
                Class<? extends SpringClientFactory> aClass = springClientFactory.getClass();
                Field contextsField = aClass.getSuperclass().getDeclaredField(CONTEXTS_FIELD);
                contextsField.setAccessible(true);
                Map<String, AnnotationConfigApplicationContext> map = (Map<String, AnnotationConfigApplicationContext>)contextsField.get(springClientFactory);
              
                for (Map.Entry<String, AnnotationConfigApplicationContext> entry : map.entrySet()) {
                    String serverName = entry.getKey();
                    AnnotationConfigApplicationContext context = entry.getValue();
                    ILoadBalancer iLoadBalancer = context.getBean(RIBBON_LOAD_BALANCER, ILoadBalancer.class);
                    if (iLoadBalancer instanceof ZoneAwareLoadBalancer) {
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

   
    public void updateRibbonCache(){
        try {
            Map<String, ZoneAwareLoadBalancer> loadBalancerMap = zoneAwareLoadBalancerMap();
            for (Map.Entry<String, ZoneAwareLoadBalancer> entry : loadBalancerMap.entrySet()) {
                ZoneAwareLoadBalancer balancer = entry.getValue();
                Class<? extends ZoneAwareLoadBalancer> zoneAwareLoadBalancerClass = balancer.getClass();
                Field updateActionField = zoneAwareLoadBalancerClass.getSuperclass().getDeclaredField(UPDATE_ACTION_FIELD);
                updateActionField.setAccessible(true);
                ServerListUpdater.UpdateAction updateAction = (ServerListUpdater.UpdateAction)updateActionField.get(balancer);
                updateAction.doUpdate();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public Map<String,Map<String,List<Server>>> getRibbonCache(){
        Map<String,Map<String,List<Server>>> taotalMap = new HashMap(256);
        try {
            Map<String, ZoneAwareLoadBalancer> zoneAwareLoadBalancerMap = zoneAwareLoadBalancerMap();
            for (Map.Entry<String, ZoneAwareLoadBalancer> entry : zoneAwareLoadBalancerMap.entrySet()) {
                String serverName = entry.getKey();
                ZoneAwareLoadBalancer balancer = entry.getValue();
                Class<? extends ZoneAwareLoadBalancer> aClass1 = balancer.getClass();
                Field allServerListField = aClass1.getSuperclass().getSuperclass().getDeclaredField(ALL_SERVER_LIST_FIELD);
                allServerListField.setAccessible(true);
                List<Server> allServerList = (List<Server>)allServerListField.get(balancer);
                Field upServerListField = aClass1.getSuperclass().getSuperclass().getDeclaredField(UP_SERVER_LIST_FIELD);
                upServerListField.setAccessible(true);
                List<Server> upServerList = (List<Server>)upServerListField.get(balancer);
                Map<String,List<Server>> mapServerList = new HashMap(8);
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
