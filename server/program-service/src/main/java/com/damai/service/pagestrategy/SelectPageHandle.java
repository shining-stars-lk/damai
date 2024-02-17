package com.damai.service.pagestrategy;

import com.damai.dto.ProgramPageListDto;
import com.damai.page.PageVo;
import com.damai.vo.ProgramListVo;

public interface SelectPageHandle {
    
    PageVo<ProgramListVo> selectPage(ProgramPageListDto programPageListDto);
    
    String getType();
}
