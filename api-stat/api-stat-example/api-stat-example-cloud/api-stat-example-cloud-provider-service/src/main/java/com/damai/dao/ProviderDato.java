package com.damai.dao;

import com.damai.dto.InfoDto;
import com.damai.vo.InfoVo;
import org.springframework.stereotype.Repository;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-12
 **/
@Repository
public class ProviderDato {
    
    public InfoVo getInfo(InfoDto infoDto){
        InfoVo infoVo = new InfoVo();
        infoVo.setId(infoDto.getCode() + "111");
        infoVo.setCode(infoDto.getCode());
        infoVo.setName("测试");
        return infoVo;
    }
}
