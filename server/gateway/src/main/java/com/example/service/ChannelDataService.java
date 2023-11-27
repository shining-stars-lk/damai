package com.example.service;

import com.example.client.BaseDataClient;
import com.example.common.ApiResponse;
import com.example.dto.GetChannelDataByCodeDto;
import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;
import com.example.vo.GetChannelDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-05-04
 **/
@Service
public class ChannelDataService {

    @Autowired
    private BaseDataClient baseDataClient;
    
    public GetChannelDataVo getChannelDataByCode(String code){
        GetChannelDataByCodeDto getChannelDataByCodeDto = new GetChannelDataByCodeDto();
        getChannelDataByCodeDto.setCode(code);
        ApiResponse<GetChannelDataVo> getChannelDataApiResponse = baseDataClient.getByCode(getChannelDataByCodeDto);
        if (getChannelDataApiResponse.getCode() == BaseCode.SUCCESS.getCode()) {
            return getChannelDataApiResponse.getData();
        }
        throw new CookFrameException("没有找到ChannelData");
    }
}
