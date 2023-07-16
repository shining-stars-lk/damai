package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-09
 **/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CourseCaseApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CourseCaseApplication.class, args);
    }
}
