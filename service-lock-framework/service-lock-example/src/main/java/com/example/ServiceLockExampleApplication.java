package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"com.example.mapper"})
@EnableTransactionManagement
@SpringBootApplication
public class ServiceLockExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceLockExampleApplication.class, args);
    }

}
