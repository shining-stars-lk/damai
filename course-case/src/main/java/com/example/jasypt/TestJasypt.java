package com.example.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-19
 **/
@Component
public class TestJasypt {
    
    @Autowired
    private StringEncryptor encryptor;
    
    @PostConstruct
    public void test(){
        //要加密的内容
        String content = "qaz123";
        String name = encryptor.encrypt(content);
        System.out.println("密文: " + name);
        System.out.println("明文" + encryptor.decrypt(name));
    }
}
