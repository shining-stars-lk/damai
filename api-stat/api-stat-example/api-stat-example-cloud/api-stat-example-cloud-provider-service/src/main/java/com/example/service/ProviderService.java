package com.example.service;

import com.example.dao.ProviderDato;
import com.example.dto.InfoDto;
import com.example.vo.InfoVo;
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
public class ProviderService {
    
    @Autowired
    private ProviderDato providerDato;
    
    public InfoVo getInfo(InfoDto infoDto){
        return providerDato.getInfo(infoDto);
    }
}
