package com.example.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.dto.ChannelDataAddDto;
import com.example.dto.GetChannelDataByCodeDto;
import com.example.entity.ChannelData;
import com.example.enums.Status;
import com.example.mapper.ChannelDataMapper;
import com.example.util.DateUtils;
import com.example.vo.GetChannelDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    public void add(ChannelDataAddDto channelDataAddDto) {
        ChannelData channelData = new ChannelData();
        BeanUtils.copyProperties(channelDataAddDto,channelData);
        channelData.setId(String.valueOf(uidGenerator.getUID()));
        channelData.setCreateTime(DateUtils.now());
        channelDataMapper.insert(channelData);
    }
}
