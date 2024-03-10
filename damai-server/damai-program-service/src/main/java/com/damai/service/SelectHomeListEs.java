package com.damai.service;

import com.damai.dto.ProgramListDto;
import com.damai.util.BusinessEsHandle;
import com.damai.vo.ProgramListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: es主页查询
 * @author: 阿宽不是程序员
 **/
@Component
public class SelectHomeListEs {
    
    @Autowired
    private BusinessEsHandle businessEsHandle;
    
    public Map<String, List<ProgramListVo>> selectHomeList(ProgramListDto programPageListDto) {
        Map<String,List<ProgramListVo>> programListVoMap = new HashMap<>(256);
        //TODO
        return null;
    }
}
