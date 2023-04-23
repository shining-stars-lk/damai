package com.example.transactioncase.controller;

import com.example.transactioncase.entity.Test;
import com.example.transactioncase.service.ITestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
        test.setNumber(id);
        int result = 0;
        try {
            result = testService.insert(test);
        }catch (Exception e) {
            log.error("出现异常",e);
        }
        return result;
    }
    @RequestMapping("getById/{id}")
    public Test getById(@PathVariable Long id){
        return testService.getById(id);
    }

    @RequestMapping("updateNumberById/{number}/{id}")
    public Integer updateNumberById(@PathVariable Long number,@PathVariable Long id){
        return testService.updateNumberById(number,id);
    }

}
