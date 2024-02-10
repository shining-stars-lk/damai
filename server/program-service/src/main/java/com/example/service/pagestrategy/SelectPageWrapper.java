package com.example.service.pagestrategy;

import com.example.dto.ProgramPageListDto;
import com.example.page.PageVo;
import com.example.vo.ProgramListVo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SelectPageWrapper {
    
    private String selectPageHandleType;
    
    private SelectPageStrategyContext selectPageStrategyContext;
    
    public PageVo<ProgramListVo> selectPage(ProgramPageListDto programPageListDto) {
        return selectPageStrategyContext.get(selectPageHandleType).selectPage(programPageListDto);
    }
}
