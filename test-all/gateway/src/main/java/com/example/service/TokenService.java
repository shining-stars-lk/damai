package com.example.service;

import com.alibaba.fastjson.JSON;
import com.example.core.CacheKeyEnum;
import com.example.core.StringUtil;
import com.example.enums.BaseCode;
import com.example.exception.ToolkitException;
import com.example.redis.CacheKeyWrap;
import com.example.redis.DistributCache;
import com.example.util.DesAlgorithm;
import com.example.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-06-03
 **/

@Component
public class TokenService {
    
    @Autowired
    private DistributCache distributCache;
    
    public String parseToken(String token){
        Map map = JSON.parseObject(token, Map.class);
        String aesKey = (String)map.get("aesKey");
        String tokenContent = (String)map.get("tokenContent");
        String userId = DesAlgorithm.decrypt(tokenContent, aesKey);
        return Optional.ofNullable(userId).filter(StringUtil::isNotEmpty)
                .orElseThrow(() -> new ToolkitException(BaseCode.USER_ID_EMPTY));
    }
    
    public UserVo getUser(String token){
        String userId = parseToken(token);
        UserVo userVo = distributCache.get(CacheKeyWrap.cacheKeyBuild(CacheKeyEnum.USER_ID, userId), UserVo.class);
        return Optional.ofNullable(userVo).orElseThrow(() -> new ToolkitException(BaseCode.USER_EMPTY));
    }
}
