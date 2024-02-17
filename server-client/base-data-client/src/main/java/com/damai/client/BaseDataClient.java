package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.AreaGetDto;
import com.damai.dto.AreaSelectDto;
import com.damai.dto.GetChannelDataByCodeDto;
import com.damai.vo.AreaVo;
import com.damai.vo.GetChannelDataVo;
import com.damai.vo.TokenDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Component
@FeignClient(value = "base-data-service",fallback = BaseDataClientFallback.class)
public interface BaseDataClient {
    
    @PostMapping("/channel/data/getByCode")
    ApiResponse<GetChannelDataVo> getByCode(GetChannelDataByCodeDto dto);
    
    @PostMapping(value = "/get")
    ApiResponse<TokenDataVo> get();
    
    @PostMapping(value = "/area/selectByIdList")
    ApiResponse<List<AreaVo>> selectByIdList(AreaSelectDto areaSelectDto);
    
    @PostMapping(value = "/area/getById")
    ApiResponse<AreaVo> getById(AreaGetDto areaGetDto);
}
