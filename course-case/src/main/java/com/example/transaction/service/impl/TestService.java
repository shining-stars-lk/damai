package com.example.transaction.service.impl;


import com.example.transaction.entity.Test;
import com.example.transaction.mapper.TestMapper;
import com.example.transaction.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackForClassName = {"Exception"})
public class TestService implements ITestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public int insert(Test test) {
        return testMapper.insert(test);
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
