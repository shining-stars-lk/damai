package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.example.core.RedisKeyEnum;
import com.example.core.StringUtil;
import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;
import com.example.jwt.TokenUtil;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-03
 **/

@Component
public class TokenService {
    
    private static final String TOKEN_SECRET = "CSYZWECHAT";
    
    @Autowired
    private RedisCache redisCache;
    
    public String parseToken(String token){
        String userStr = TokenUtil.parseToken(token,TOKEN_SECRET);
        if (StringUtil.isNotEmpty(userStr)) {
            return JSONObject.parseObject(userStr).getString("userId");
        }
        return null;
    }
    
    public UserVo getUser(String token){
        UserVo userVo = null;
        String userId = parseToken(token);
        if (StringUtil.isNotEmpty(userId)) {
            userVo = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID, userId), UserVo.class);
        }
        return Optional.ofNullable(userVo).orElseThrow(() -> new CookFrameException(BaseCode.USER_EMPTY));
    }
}
