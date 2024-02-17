package com.damai.service.pagestrategy;

import com.damai.dto.ProgramPageListDto;
import com.damai.page.PageVo;
import com.damai.vo.ProgramListVo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SelectPageWrapper {
    
    private String selectPageHandleType;
    
    private SelectPageStrategyContext selectPageStrategyContext;
    
    public PageVo<ProgramListVo> selectPage(ProgramPageListDto programPageListDto) {
        return selectPageStrategyContext.get(selectPageHandleType).selectPage(programPageListDto);
    }
}
