package com.damai.service;

import com.damai.dao.ExampleSingleDao;
import com.damai.util.Time;
import com.damai.vo.UserVo;
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
