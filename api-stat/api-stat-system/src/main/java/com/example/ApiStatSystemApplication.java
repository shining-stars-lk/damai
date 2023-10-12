package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ApiStatSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiStatSystemApplication.class, args);
    }

}
