package com.example.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dto.TokenDataDto;
import com.example.entity.TokenData;
import com.example.enums.Status;
import com.example.mapper.TokenDataMapper;
import com.example.util.DateUtils;
import com.example.vo.TokenDataVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-07-05
 **/
@Service
public class TokenDataService {

    @Autowired
    private TokenDataMapper tokenDataMapper;
    @Resource
    private UidGenerator uidGenerator; 
    
    public void add(TokenDataDto tokenDataDto){
        TokenData tokenData = new TokenData();
        BeanUtils.copyProperties(tokenDataDto,tokenData);
        tokenData.setId(String.valueOf(uidGenerator.getUID()));
        tokenData.setCreateTime(DateUtils.now());
        tokenDataMapper.insert(tokenData);
    }
    
    public TokenDataVo get() {
        TokenDataVo tokenDataVo = new TokenDataVo();
        LambdaQueryWrapper<TokenData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TokenData::getStatus, Status.RUN.getCode());
        Optional.ofNullable(tokenDataMapper.selectOne(queryWrapper)).ifPresent(tokenData -> {
            BeanUtils.copyProperties(tokenData,tokenDataVo);
        });
        return tokenDataVo;
    }
}
