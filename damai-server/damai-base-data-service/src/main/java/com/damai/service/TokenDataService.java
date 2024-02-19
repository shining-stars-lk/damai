package com.damai.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.damai.dto.TokenDataDto;
import com.damai.entity.TokenData;
import com.damai.enums.Status;
import com.damai.mapper.TokenDataMapper;
import com.damai.util.DateUtils;
import com.damai.vo.TokenDataVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: token service
 * @author: 阿宽不是程序员
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
        tokenData.setId(uidGenerator.getUid());
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
