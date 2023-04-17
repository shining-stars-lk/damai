package com.example.controller;


import com.nacosrefresh.handle.NacosHandle;
import com.nacosrefresh.handle.ServiceHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lk
 * @create: 2022-06-08
 **/
@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired(required = false)
    private ServiceHandle serviceHandle;
    @Autowired(required = false)
    private NacosHandle nacosHandle;




    /**
     * 更新并拉取服务列表
     * */
    @RequestMapping(value = "/updatenacosandribboncache", method = RequestMethod.POST)
    public Boolean updateNacosAndRibbonCache() {
        if (serviceHandle != null) {
            serviceHandle.updateNacosAndRibbonCache();
        }
        return true;

    }

    /**
     * 获取ribbon和nacos缓存服务列表
     * */
    @RequestMapping(value = "/getnacosandribboncache", method = RequestMethod.POST)
    public Map getNacosAndRibbonCache() {
        if (serviceHandle != null) {
            serviceHandle.getNacosAndRibbonCache();
        }
        return new HashMap();
    }

    /**
     * 从nacos主动下线
     * */
    @RequestMapping(value = "/stopservice", method = RequestMethod.POST)
    public Boolean stopService(HttpServletRequest request){
        if (!request.getServerName().equalsIgnoreCase("localhost")) {
            return false;
        }
        if (nacosHandle != null) {
            return nacosHandle.stopService();
        }
        return false;
    }
}
