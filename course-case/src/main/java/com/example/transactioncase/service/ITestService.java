package com.example.transactioncase.service;

import com.example.transactioncase.entity.Test;

public interface ITestService {

    int insert(Test test);
    
    int insert2(Test test);

    Test getById(Long id);

    Integer updateNumberById(Long number, Long id);
}
