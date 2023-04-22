package com.example.transaction.service;

import com.example.transaction.entity.Test;

public interface ITestService {

    int insert(Test test);

    Test getById(Long id);

    Integer updateNumberById(Long number, Long id);
}
