package com.damai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-09-29
 **/

public class AdminConfig {
    
    @Primary
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomEnhance(){
        return new JacksonCustomEnhance();
    }
    
    @Bean
    public OpenAPI customOpenApi() {
        
        return new OpenAPI()
                .info(new Info()
                        .title("前端使用")
                        .version("1.0")
                        .description("项目学习")
                        .contact(new Contact()
                                .name("阿星不是程序员")
                        ));
        
    }
}
