package com.damai.service;

import com.damai.entity.Test1;
import com.damai.mapper.Test1Mapper;
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
public class Test1Service {

    @Autowired
    private Test1Mapper test1Mapper;
    
    @Autowired
    private Test2Service test2Service;
    
    @Transactional(rollbackFor=Exception.class)
    public void add(Long id,String code,String name){
        Test1 test1 = new Test1();
        test1.setId(id);
        test1.setCode(code);
        test1Mapper.insert(test1);
//        try {
//            test2Service.add(id,name);
//        }catch (Exception e) {
//            System.out.println("异常信息:"+e.getMessage());
//        }
    }
    
    @Transactional(rollbackFor=Exception.class)
    public void add2(Long id,String code,String name){
        Test1 test1 = new Test1();
        test1.setId(id);
        test1.setCode(code);
        test1Mapper.insert(test1);
        try {
            test2Service.add(id,name);
        }catch (Exception e) {
            System.out.println("test2添加出现了异常，异常信息:"+e.getMessage());
        }
        System.out.println("添加方法结束");
    }
}
