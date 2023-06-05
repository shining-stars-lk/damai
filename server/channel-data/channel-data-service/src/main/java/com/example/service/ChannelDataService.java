package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.ChannelData;
import com.example.mapper.ChannelDataMapper;
import com.example.vo.GetChannelDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Service
@Slf4j
public class ChannelDataService {
    
    @Autowired
    private ChannelDataMapper channelDataMapper;
    
    public GetChannelDataVo getByCode(String code){
        QueryWrapper<ChannelData> wrapper = new QueryWrapper();
        wrapper.eq("status",1).eq("code",code);
        ChannelData channelData = channelDataMapper.selectOne(wrapper);
        GetChannelDataVo getChannelDataVo = new GetChannelDataVo();
        BeanUtils.copyProperties(channelData,getChannelDataVo);
        return getChannelDataVo;
    }
}
