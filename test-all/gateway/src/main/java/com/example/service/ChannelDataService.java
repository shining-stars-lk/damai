package com.example.service;

import com.example.client.ChannelDataClient;
import com.example.common.Result;
import com.example.enums.BaseCode;
import com.example.vo.GetChannelDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-05-04
 **/
@Service
public class ChannelDataService {

    @Autowired
    private ChannelDataClient channelDataClient;
    
    public GetChannelDataVo GetChannelDataByCode(String code){
        Result<GetChannelDataVo> GetChannelDataResult = channelDataClient.getByCode(code);
        if (GetChannelDataResult.getCode() == BaseCode.SUCCESS.getCode()) {
            return GetChannelDataResult.getData();
        }
        throw new RuntimeException("没有找到ChannelData");
    }
}
