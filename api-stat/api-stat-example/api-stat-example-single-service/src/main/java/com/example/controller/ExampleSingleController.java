package com.example.controller;

import com.example.service.ExampleSingleService;
import com.example.util.Time;
import com.example.vo.UserVo;
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
