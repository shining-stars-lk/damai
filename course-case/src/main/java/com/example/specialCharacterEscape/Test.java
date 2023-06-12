package com.example.specialCharacterEscape;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-12
 **/
@Component
public class Test {
    
    @Value("${test.password1}")
    private String password1;
    
    @Value("${test.password2}")
    private String password2;
    
    @PostConstruct
    public void postConstruct() {
        System.out.println("password1 : " + password1);
        System.out.println("password2 : " + password2);
    }
}
