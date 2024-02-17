package com.damai.service;

import com.alibaba.fastjson.JSONObject;
import com.damai.core.RedisKeyEnum;
import com.damai.core.StringUtil;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.jwt.TokenUtil;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyWrap;
import com.damai.vo.UserVo;
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
        return Optional.ofNullable(userVo).orElseThrow(() -> new DaMaiFrameException(BaseCode.USER_EMPTY));
    }
}
