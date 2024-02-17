package com.damai.refresh.custom;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 服务管理
 * @author: 星哥
 * @create: 2023-06-08
 **/
public class NacosAndRibbonCustom {

    private NacosCustom nacosCustom;

    private RibbonCustom ribbonCustom;

    public NacosAndRibbonCustom(NacosCustom nacosCustom, RibbonCustom ribbonCustom){
        this.nacosCustom = nacosCustom;
        this.ribbonCustom = ribbonCustom;
    }

    public boolean refreshNacosAndRibbonCache(){
        nacosCustom.clearNacosCache();
        ribbonCustom.updateRibbonCache();
        return true;
    }

    public Map getNacosAndRibbonCacheList() {
        Map nacosCache = nacosCustom.getNacosCache();
        Map ribbonCache = ribbonCustom.getRibbonCache();
        Map map = new HashMap();
        map.put("nacosCache",nacosCache);
        map.put("ribbonCache",ribbonCache);
        return map;
    }
}
