package com.example.service;

import com.example.entity.Test;

public interface ITestService {

    int insert(Test test);
    
    int insert2(Test test);

    Test getById(Long id);

    Integer updateNumberById(Long number, Long id);
    
    boolean insertNumber(Long number, Long id);
}
