package com.example.service;

import com.example.dao.ExampleSingleDao;
import com.example.util.Time;
import com.example.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-08
 **/
@Service
public class ExampleSingleService {
    
    @Autowired
    private ExampleSingleDao exampleSingleDao;
    
    public UserVo getUser(String userId){
        Time.simulationTime();
        return exampleSingleDao.getUser(userId);
    }
}
