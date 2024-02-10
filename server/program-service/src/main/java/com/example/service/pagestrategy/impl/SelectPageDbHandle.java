package com.example.service.pagestrategy.impl;

import com.example.dto.ProgramPageListDto;
import com.example.page.PageVo;
import com.example.service.ProgramService;
import com.example.service.pagestrategy.ProgramConstant;
import com.example.service.pagestrategy.SelectPageHandle;
import com.example.vo.ProgramListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SelectPageDbHandle implements SelectPageHandle {
    
    @Autowired
    private ProgramService programService;
    
    
    @Override
    public PageVo<ProgramListVo> selectPage(ProgramPageListDto programPageListDto) {
        return programService.doSelectPage(programPageListDto);
    }
    
    @Override
    public String getType() {
        return ProgramConstant.DB_TYPE_NAME;
    }
}
