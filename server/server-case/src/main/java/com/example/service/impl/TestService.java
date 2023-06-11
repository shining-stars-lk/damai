package com.example.service.impl;


import com.example.entity.Test;
import com.example.mapper.TestMapper;
import com.example.service.ITestService;
//import com.tool.servicelock.annotion.ServiceLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TestService implements ITestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public int insert(Test test) {
        testMapper.insert(test);
        return 1;
    }
    
    @Transactional
    @Override
    public int insert2(Test test) {
        Long id = test.getId();
        test.setId(id+10);
        int result = testMapper.insert(test);
        return result;
    }

    @Override
    public Test getById(Long id) {
        return testMapper.getById(id);
    }

    @Override
    public Integer updateNumberById(Long number, Long id) {
        return testMapper.updateNumberById(number, id);
    }
    
    
    @Override
    @Transactional
    //@ServiceLock(name = "insertNumber",keys = {"#id"},waitTime = 50)
    public boolean insertNumber(final Long number, final Long id) {
        Test test = testMapper.getById(id);
        Long originalNumber = test.getNumber();
        originalNumber = originalNumber + number;
        test.setNumber(originalNumber);
        testMapper.updateById(test);
        return true;
    }
    
}
