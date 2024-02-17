package com.damai.service;

import com.damai.client.ProviderClient;
import com.damai.common.ApiResponse;
import com.damai.dto.InfoDto;
import com.damai.vo.InfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Service
@Slf4j
public class ConsumerService {
    
    @Autowired
    private ProviderClient providerClient;
    
    public ApiResponse<InfoVo> getInfo(final InfoDto infoDto) {
        return providerClient.getInfo(infoDto);
    }
}
