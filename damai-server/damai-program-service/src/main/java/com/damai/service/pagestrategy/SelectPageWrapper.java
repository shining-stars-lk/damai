package com.damai.service.pagestrategy;

import com.damai.dto.ProgramPageListDto;
import com.damai.page.PageVo;
import com.damai.vo.ProgramListVo;
import lombok.AllArgsConstructor;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目查询策略包装
 * @author: 阿宽不是程序员
 **/
@AllArgsConstructor
public class SelectPageWrapper {
    
    private String selectPageHandleType;
    
    private SelectPageStrategyContext selectPageStrategyContext;
    
    public PageVo<ProgramListVo> selectPage(ProgramPageListDto programPageListDto) {
        return selectPageStrategyContext.get(selectPageHandleType).selectPage(programPageListDto);
    }
}
