package com.damai.controller;

import com.damai.service.ExampleSingleService;
import com.damai.util.Time;
import com.damai.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-08
 **/
@RestController
@RequestMapping(value = "/exampleSingle")
public class ExampleSingleController {
    
    @Autowired
    private ExampleSingleService exampleSingleService;
    
    @RequestMapping(value = "/getUser")
    public UserVo getUser(String userId){
        Time.simulationTime();
        return exampleSingleService.getUser(userId);
    }
}
