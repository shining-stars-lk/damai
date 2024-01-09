package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.client.BaseDataClient;
import com.example.core.RedisKeyEnum;
import com.example.dto.UserIdDto;
import com.example.dto.UserMobileDto;
import com.example.dto.UserRegisterDto;
import com.example.dto.UserUpdateDto;
import com.example.entity.User;
import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;
import com.example.jwt.TokenUtil;
import com.example.mapper.UserMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.redisson.LockType;
import com.example.servicelock.annotion.ServiceLock;
import com.example.util.RBloomFilterUtil;
import com.example.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.core.DistributedLockConstants.REGISTER_USER_LOCK;

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
    @ServiceLock(lockType= LockType.Write,name = REGISTER_USER_LOCK,keys = {"#userRegisterDto.mobile"})
    public void register(final UserRegisterDto userRegisterDto) {
        exist(userRegisterDto.getMobile());
        
        User user = new User();
        BeanUtils.copyProperties(userRegisterDto,user);
        user.setId(uidGenerator.getUID());
        userMapper.insert(user);
        
        rBloomFilterUtil.add(user.getMobile());
    }
    
    @ServiceLock(lockType= LockType.Read,name = REGISTER_USER_LOCK,keys = {"#mobile"})
    public void exist(String mobile){
        boolean contains = rBloomFilterUtil.contains(mobile);
        if (contains) {
            LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                    .eq(User::getMobile, mobile);
            User user = userMapper.selectOne(queryWrapper);
            if (Objects.nonNull(user)) {
                throw new CookFrameException(BaseCode.USER_EXIST);
            }
        }
    }
    
    public String login(final UserMobileDto userMobileDto) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getMobile, userMobileDto.getMobile());
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        Boolean loginResult = redisCache.hasKey(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID,user.getId()));
        if (loginResult) {
            throw new CookFrameException(BaseCode.USER_LOG_IN);
        }
        redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID,user.getId()),user,tokenExpireTime + 1000,TimeUnit.MILLISECONDS);
        return createToken(user.getId());
    }
    
    public String createToken(Long userId){
        Map<String,Object> map = new HashMap<>(4);
        map.put("userId",userId);
        return TokenUtil.createToken(String.valueOf(uidGenerator.getUID()), JSON.toJSONString(map),tokenExpireTime,TOKEN_SECRET);
    }
    
    public void logOut(UserMobileDto userMobileDto) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getMobile, userMobileDto.getMobile());
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        redisCache.del(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID,user.getId()));
    }
    
    public void update(UserUpdateDto userUpdateDto){
        User user = userMapper.selectById(userUpdateDto.getId());
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        User updateUser = new User();
        BeanUtil.copyProperties(userUpdateDto,updateUser);
        userMapper.updateById(user);
    }
    
    public UserVo getByMobile(UserMobileDto userMobileDto) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getMobile, userMobileDto.getMobile());
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user,userVo);
        return userVo;
    }
    
    public UserVo getById(UserIdDto userIdDto) {
        User user = userMapper.selectById(userIdDto.getId());
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user,userVo);
        return userVo;
    }
}
