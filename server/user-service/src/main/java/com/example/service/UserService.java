package com.example.service;

import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.client.BaseDataClient;
import com.example.core.RedisKeyEnum;
import com.example.dto.RegisterUserDto;
import com.example.dto.UserDto;
import com.example.dto.logOutDto;
import com.example.entity.User;
import com.example.enums.BusinessStatus;
import com.example.jwt.TokenUtil;
import com.example.mapper.UserMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.util.DateUtils;
import com.example.util.RBloomFilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    private static final String TOKEN_SECRET = "CSYZWECHAT";
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UidGenerator uidGenerator;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private BaseDataClient baseDataClient;
    
    @Autowired
    private RBloomFilterUtil rBloomFilterUtil;
    
    @Value("${token.expire.time:86400000}")
    private Long tokenExpireTime;
    
    @Transactional
    public String login(final UserDto userDto) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getMobile, userDto.getMobile());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            user = new User();
            BeanUtils.copyProperties(userDto,user);
            user.setId(uidGenerator.getUID());
            user.setCreateTime(new Date());
            userMapper.insert(user);
        }else {
            user.setEditTime(new Date());
            userMapper.updateById(user);
        }
        cacheUser(user.getMobile());
        return createToken(user.getId());
    }
    
    public String createToken(Long userId){
        Map<String,Object> map = new HashMap<>(4);
        map.put("userId",userId);
        return TokenUtil.createToken(String.valueOf(uidGenerator.getUID()), JSON.toJSONString(map),tokenExpireTime,TOKEN_SECRET);
    }
    
    @Transactional
    public void logOut(final logOutDto logOutDto) {
        User user = new User();
        user.setMobile(logOutDto.getMobile());
        user.setLoginStatus(BusinessStatus.NO.getCode());
        user.setEditTime(DateUtils.now());
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate(User.class)
                .eq(User::getMobile,logOutDto.getMobile());
        userMapper.update(user,updateWrapper);
        delCacheUser(user.getMobile());
    }
    
    public void cacheUser(String mobile){
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getMobile, mobile);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID,user.getId()),user);
            redisCache.expire(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID,user.getId()),tokenExpireTime + 1000,TimeUnit.MILLISECONDS);
        }
    }
    @Transactional
    public void delCacheUser(String mobile){
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getMobile, mobile);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            redisCache.del(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID,user.getId()));
        }
    }
    
    public void register(final RegisterUserDto registerUserDto) {
        User user = new User();
        BeanUtils.copyProperties(registerUserDto,user);
        user.setId(uidGenerator.getUID());
        userMapper.insert(user);
    }
}
