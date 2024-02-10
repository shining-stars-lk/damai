package com.example.service.pagestrategy;

import com.example.dto.ProgramPageListDto;
import com.example.page.PageVo;
import com.example.vo.ProgramListVo;

public interface SelectPageHandle {
    
    PageVo<ProgramListVo> selectPage(ProgramPageListDto programPageListDto);
    
    String getType();
}
