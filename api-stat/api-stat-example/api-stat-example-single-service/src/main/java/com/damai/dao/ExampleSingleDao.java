package com.damai.dao;

import com.damai.util.Time;
import com.damai.vo.UserVo;
import org.springframework.stereotype.Repository;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-08
 **/
@Repository
public class ExampleSingleDao {
    
    public UserVo getUser(String userId){
        Time.simulationTime();
        UserVo userVo = new UserVo();
        userVo.setUserId(userId);
        userVo.setName("张三");
        return userVo;
    }
}
