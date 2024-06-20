package com.damai.registercenter;

import com.damai.util.StringUtil;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Optional;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 选择注册中心执行
 * @author: 阿星不是程序员
 **/
public class RegisterCenterAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware {
    
    private Environment environment;
    
    private static final String NACOS = "nacos";
    
    private static final String EUREKA = "eureka";
    
    private static final Map<String,String> EUREKA_AUTO_CONFIGURATION_BEAN_NAME_MAP = EurekaAutoConfigurationBean.autoConfigurationBeanNameMap();
    
    private static final Map<String,String> NACOS_AUTO_CONFIGURATION_BEAN_NAME_MAP = NacosAutoConfigurationBean.autoConfigurationBeanNameMap(); 
    
    
    @Override
    public boolean[] match(final String[] autoConfigurationClasses, final AutoConfigurationMetadata autoConfigurationMetadata) {
        String type = Optional.ofNullable(environment.getProperty("service.register.type")).filter(StringUtil::isNotEmpty).orElse(NACOS);
        
        boolean[] match = new boolean[autoConfigurationClasses.length];
        
        if (NACOS.equals(type)) {
            for (int i = 0; i < autoConfigurationClasses.length; i++) {
                match[i] = EUREKA_AUTO_CONFIGURATION_BEAN_NAME_MAP.get(autoConfigurationClasses[i]) == null;
            }
        }else if (EUREKA.equals(type)) {
            for (int i = 0; i < autoConfigurationClasses.length; i++) {
                match[i] = NACOS_AUTO_CONFIGURATION_BEAN_NAME_MAP.get(autoConfigurationClasses[i]) == null;
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
