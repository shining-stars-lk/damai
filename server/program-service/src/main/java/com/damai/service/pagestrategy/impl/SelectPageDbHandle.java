package com.damai.service.pagestrategy.impl;

import com.damai.dto.ProgramPageListDto;
import com.damai.page.PageVo;
import com.damai.service.ProgramService;
import com.damai.service.pagestrategy.ProgramConstant;
import com.damai.service.pagestrategy.SelectPageHandle;
import com.damai.vo.ProgramListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 数据库查询
 * @author: 阿宽不是程序员
 **/
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
