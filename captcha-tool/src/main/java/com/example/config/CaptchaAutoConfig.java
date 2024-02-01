package com.example.config;

import com.anji.captcha.service.CaptchaService;
import com.example.service.CaptchaHandle;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-02-01
 **/
public class CaptchaAutoConfig {
    
    @Bean
    public CaptchaHandle captchaHandle(CaptchaService captchaService){
        return new CaptchaHandle(captchaService);
    }
}
