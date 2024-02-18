package com.damai.refresh.custom;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: nacos和ribbon定制化
 * @author: 阿宽不是程序员
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
