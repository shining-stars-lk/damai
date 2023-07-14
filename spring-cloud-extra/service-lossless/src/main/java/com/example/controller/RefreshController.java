package com.example.controller;


import com.example.refresh.custom.NacosCustom;
import com.example.refresh.custom.NacosAndRibbonCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: kuan
 * @create: 2023-06-08
 **/
@RestController
@RequestMapping("/refresh")
public class RefreshController {

    @Autowired(required = false)
    private NacosAndRibbonCustom nacosAndRibbonCustom;
    @Autowired(required = false)
    private NacosCustom nacosCustom;




    /**
     * 更新并拉取服务列表
     * */
    @RequestMapping(value = "/refreshNacosAndRibbonCache", method = RequestMethod.POST)
    public Boolean refreshNacosAndRibbonCache() {
        if (nacosAndRibbonCustom != null) {
            nacosAndRibbonCustom.refreshNacosAndRibbonCache();
        }
        return true;

    }

    /**
     * 获取ribbon和nacos缓存服务列表
     * */
    @RequestMapping(value = "/getNacosAndRibbonCacheList", method = RequestMethod.POST)
    public Map getNacosAndRibbonCacheList() {
        if (nacosAndRibbonCustom != null) {
            nacosAndRibbonCustom.getNacosAndRibbonCacheList();
        }
        return new HashMap();
    }

    /**
     * 从nacos主动下线
     * */
    @RequestMapping(value = "/logoutService", method = RequestMethod.POST)
    public Boolean logoutService(HttpServletRequest request){
        if (!(request.getServerName().equalsIgnoreCase("localhost") || request.getServerName().equalsIgnoreCase("127.0.0.1"))) {
            return false;
        }
        if (nacosCustom != null) {
            return nacosCustom.LogoutService();
        }
        return false;
    }
}
