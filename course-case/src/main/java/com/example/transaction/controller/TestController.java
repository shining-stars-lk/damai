package com.example.transaction.controller;

import com.example.transaction.entity.Test;
import com.example.transaction.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ITestService testService;

    @RequestMapping("/insert/{id}")
    public Integer insert(@PathVariable Long id){
        Test test = new Test();
        test.setId(id);
        test.setColumn1("test1-" + id);
        test.setColumn2("test2-" + id);
        test.setColumn3("test3-" + id);
        test.setColumn4("test4-" + id);
        test.setColumn5("test5-" + id);
        test.setColumn6("test6-" + id);
        return testService.insert(test);
    }
    @RequestMapping("getById/{id}")
    public Test getById(@PathVariable Long id){
        return testService.getById(id);
    }

}
