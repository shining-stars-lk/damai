package com.example.service;

import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.client.BaseDataClient;
import com.example.core.RedisKeyEnum;
import com.example.dto.UserDto;
import com.example.dto.logOutDto;
import com.example.entity.User;
import com.example.enums.UserLogStatus;
import com.example.jwt.TokenUtil;
import com.example.mapper.UserMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Service
@Slf4j
public class UserService {
    
    private static final String TOKEN_SECRET = "CSYZWECHAT";
    
    @Autowired
    private UserMapper userMapper;
    
    @Resource
    private UidGenerator uidGenerator;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private BaseDataClient baseDataClient;
    
    @Value("${token.expire.time:86400000}")
    private Long tokenExpireTime;
    
    @Transactional
    public String login(final UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", userDto.getMobile());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            user = new User();
            BeanUtils.copyProperties(userDto,user);
            user.setId(String.valueOf(uidGenerator.getUID()));
            user.setCreateTime(new Date());
            userMapper.insert(user);
        }else {
            user.setEditTime(new Date());
            userMapper.updateById(user);
        }
        cacheUser(user);
        return createToken(user.getId());
    }
    
    public void cacheUser(User user){
        redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID,user.getId()),user);
        redisCache.expire(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID,user.getId()),tokenExpireTime + 1000,TimeUnit.MILLISECONDS);
    }
    
    public String createToken(String userId){
        Map<String,String> map = new HashMap<>(4);
        map.put("userId",userId);
        return TokenUtil.createToken(String.valueOf(uidGenerator.getUID()), JSON.toJSONString(map),tokenExpireTime,TOKEN_SECRET);
    }
    
    public void logOut(final logOutDto logOutDto) {
        User user = new User();
        user.setMobile(logOutDto.getMobile());
        user.setLogStatus(UserLogStatus.OUT.getCode());
        user.setEditTime(DateUtils.now());
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getMobile,logOutDto.getMobile());
        userMapper.update(user,updateWrapper);
    }
}
