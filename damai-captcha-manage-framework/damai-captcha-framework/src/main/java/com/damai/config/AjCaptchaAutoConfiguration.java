package com.damai.config;

import com.damai.properties.AjCaptchaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: AjCaptchaAutoConfiguration
 * @author: 阿星不是程序员
 **/

@Configuration
@EnableConfigurationProperties(AjCaptchaProperties.class)
@ComponentScan("com.damai")
@Import({AjCaptchaServiceAutoConfiguration.class, AjCaptchaStorageAutoConfiguration.class})
public class AjCaptchaAutoConfiguration {
}
