package com.example.service;

import com.baidu.fsg.uid.UidGenerator;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Service
@Slf4j
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Resource
    private UidGenerator uidGenerator;
    
    
    public void login(final UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        user.setId(String.valueOf(uidGenerator.getUID()));
        user.setCreateTime(new Date());
        userMapper.insert(user);
    }
    
    public void cacheUser(){
        
    }
}
