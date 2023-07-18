package com.example.registercenter;

import com.example.core.StringUtil;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Optional;

/**
 * @program: cook-frame
 * @description: 通过service.register.type = nacos/eureka 来实现注册中心的切换
 * @author: 星哥
 * @create: 2023-06-30
 **/
public class RegisterCenterAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware {
    
    private Environment environment;
    
    public String NACOS = "nacos";
    
    public String EUREKA = "eureka";
    
    public Map<String,String> eurekaAutoConfigurationBeanNameMap = EurekaAutoConfigurationBean.autoConfigurationBeanNameMap();
    
    public Map<String,String> nacosAutoConfigurationBeanNameMap = NacosAutoConfigurationBean.autoConfigurationBeanNameMap(); 
    
    
    @Override
    public boolean[] match(final String[] autoConfigurationClasses, final AutoConfigurationMetadata autoConfigurationMetadata) {
        String type = Optional.ofNullable(environment.getProperty("service.register.type")).filter(StringUtil::isNotEmpty).orElse(NACOS);
        
        boolean[] match = new boolean[autoConfigurationClasses.length];
        
        if (NACOS.equals(type)) {
            for (int i = 0; i < autoConfigurationClasses.length; i++) {
                match[i] = eurekaAutoConfigurationBeanNameMap.get(autoConfigurationClasses[i]) == null ? true : false;
            }
        }else if (EUREKA.equals(type)) {
            for (int i = 0; i < autoConfigurationClasses.length; i++) {
                match[i] = nacosAutoConfigurationBeanNameMap.get(autoConfigurationClasses[i]) == null ? true : false;
            }
        }else {
            for (int i = 0; i < autoConfigurationClasses.length; i++) {
                match[i] = true;
            }
        }
        return match;
    }
    
    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }
}
