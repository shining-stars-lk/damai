package com.example.service;

import com.example.client.ChannelDataClient;
import com.example.common.Result;
import com.example.dto.GetChannelDataByCodeDto;
import com.example.enums.BaseCode;
import com.example.exception.ToolkitException;
import com.example.vo.GetChannelDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-05-04
 **/
@Service
public class ChannelDataService {

    @Autowired
    private ChannelDataClient channelDataClient;
    
    public GetChannelDataVo getChannelDataByCode(String code){
        GetChannelDataByCodeDto getChannelDataByCodeDto = new GetChannelDataByCodeDto();
        getChannelDataByCodeDto.setCode(code);
        Result<GetChannelDataVo> GetChannelDataResult = channelDataClient.getByCode(getChannelDataByCodeDto);
        if (GetChannelDataResult.getCode() == BaseCode.SUCCESS.getCode()) {
            return GetChannelDataResult.getData();
        }
        throw new ToolkitException("没有找到ChannelData");
    }
}
