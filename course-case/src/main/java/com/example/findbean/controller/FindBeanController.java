package com.example.findbean.controller;

import com.example.findbean.ABaseBean;
import com.example.findbean.ApplicationContextHolder;
import com.example.findbean.Base;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-05-23
 **/
@RestController
public class FindBeanController {
    
    
    @RequestMapping("/finBean")
    public void findBean(){
        Base abaseBean1 = ApplicationContextHolder.getBean("baseBean", Base.class);
        ABaseBean abaseBean2 = ApplicationContextHolder.getBean("baseBean", ABaseBean.class);
        Base cBaseBean = ApplicationContextHolder.getBean("cBaseBean", Base.class);
    }
}
