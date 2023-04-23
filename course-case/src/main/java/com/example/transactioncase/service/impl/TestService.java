package com.example.transactioncase.service.impl;


import com.example.transactioncase.entity.Test;
import com.example.transactioncase.mapper.TestMapper;
import com.example.transactioncase.service.ITestService;
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
        int i = 1 / 0;
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
}
