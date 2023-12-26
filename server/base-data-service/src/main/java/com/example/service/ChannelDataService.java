package com.example.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.core.RedisKeyEnum;
import com.example.dto.ChannelDataAddDto;
import com.example.dto.GetChannelDataByCodeDto;
import com.example.entity.ChannelData;
import com.example.enums.Status;
import com.example.mapper.ChannelDataMapper;
import com.example.redis.RedisKeyWrap;
import com.example.util.DateUtils;
import com.example.vo.GetChannelDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.redis.RedisCache;
import javax.annotation.Resource;
import java.util.Optional;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Service
@Slf4j
public class ChannelDataService {
    
    @Autowired
    private ChannelDataMapper channelDataMapper;
    
    @Resource
    private UidGenerator uidGenerator;
    
    @Autowired
    private RedisCache redisCache; 
    
    public GetChannelDataVo getByCode(GetChannelDataByCodeDto dto){
        GetChannelDataVo getChannelDataVo = new GetChannelDataVo();
        LambdaQueryWrapper<ChannelData> wrapper = Wrappers.lambdaQuery(ChannelData.class)
                .eq(ChannelData::getStatus, Status.RUN.getCode())
                .eq(ChannelData::getCode,dto.getCode());
        Optional.ofNullable(channelDataMapper.selectOne(wrapper)).ifPresent(channelData -> {
            BeanUtils.copyProperties(channelData,getChannelDataVo);
        });
        return getChannelDataVo;
    }
    
    @Transactional
    public void add(ChannelDataAddDto channelDataAddDto) {
        ChannelData channelData = new ChannelData();
        BeanUtils.copyProperties(channelDataAddDto,channelData);
        channelData.setId(uidGenerator.getUID());
        channelData.setCreateTime(DateUtils.now());
        channelDataMapper.insert(channelData);
        addRedisChannelData(channelData);
    }
    
    private void addRedisChannelData(ChannelData channelData){
        GetChannelDataVo getChannelDataVo = new GetChannelDataVo();
        BeanUtils.copyProperties(channelData,getChannelDataVo);
        redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.CHANNEL_DATA,getChannelDataVo.getCode()),getChannelDataVo);
    }
}
