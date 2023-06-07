package com.example.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.client.ChannelDataClient;
import com.example.common.Result;
import com.example.core.CacheKeyEnum;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.enums.BaseCode;
import com.example.exception.ToolkitException;
import com.example.mapper.UserMapper;
import com.example.redis.CacheKeyWrap;
import com.example.redis.DistributCache;
import com.example.util.DesAlgorithm;
import com.example.vo.GetChannelDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Service
@Slf4j
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Resource
    private UidGenerator uidGenerator;
    
    @Autowired
    private DistributCache distributCache;
    
    @Autowired
    private ChannelDataClient channelDataClient;
    
    
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
        return generateToken(user.getId());
    }
    
    public void cacheUser(User user){
        distributCache.set(CacheKeyWrap.cacheKeyBuild(CacheKeyEnum.USER_ID,user.getId()),user);
    }
    
    public String generateToken(String userId){
        String aesKey = getAesKey();
        String token = DesAlgorithm.encrypt(userId, aesKey);
        return token;
    }
    
    public String getCode(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        return Optional.ofNullable(request.getHeader("code")).orElseThrow(() -> new ToolkitException(BaseCode.CODE_EMPTY));
    }
    
    public String getAesKey(){
        Result<GetChannelDataVo> result = channelDataClient.getByCode(getCode());
        if (result.getCode() != Result.success().getCode()) {
            throw new ToolkitException(result);
        }
        return Optional.ofNullable(result.getData()).map(GetChannelDataVo::getAesKey).orElseThrow(() -> new ToolkitException(BaseCode.CHANNEL_DATA));
    }
}
