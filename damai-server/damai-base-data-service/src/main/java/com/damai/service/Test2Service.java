package com.damai.service;

import com.damai.entity.Test2;
import com.damai.mapper.Test2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-10-10
 **/
@Service
public class Test2Service {

    @Autowired
    private Test2Mapper test2Mapper;
    
    @Transactional(rollbackFor=Exception.class)
    public void add(Long id,String name){
        Test2 test2 = new Test2();
        test2.setId(id);
        test2.setName(name);
        test2Mapper.insert(test2);
        throw new RuntimeException("模拟异常");
    }
}
