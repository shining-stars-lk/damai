package com.nacosrefresh.handle;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: msa-common-service-manage
 * @description: 服务管理
 * @author: lk
 * @create: 2022-06-08
 **/
public class ServiceHandle {

    private NacosHandle nacosHandle;

    private RibbonHandle ribbonHandle;

    public ServiceHandle(NacosHandle nacosHandle,RibbonHandle ribbonHandle){
        this.nacosHandle = nacosHandle;
        this.ribbonHandle = ribbonHandle;
    }

    public boolean updateNacosAndRibbonCache(){
        nacosHandle.clearNacosCache();
        ribbonHandle.updateRibbonCache();
        return true;
    }

    public Map getNacosAndRibbonCache() {
        Map nacosCache = nacosHandle.getNacosCache();
        Map ribbonCache = ribbonHandle.getRibbonCache();
        Map map = new HashMap();
        map.put("nacosCache",nacosCache);
        map.put("ribbonCache",ribbonCache);
        return map;
    }
}
