package com.example.service;

import com.example.entity.Test;

public interface ITestService {

    int insert(Test test);
    
    int insert2(Test test);

    Test getById(Long id);

    Integer updateNumberById(Integer number, Long id);
    
    boolean insertNumber(Integer number, Long id);
}
