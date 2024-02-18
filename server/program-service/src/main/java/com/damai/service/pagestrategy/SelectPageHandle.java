package com.damai.service.pagestrategy;

import com.damai.dto.ProgramPageListDto;
import com.damai.page.PageVo;
import com.damai.vo.ProgramListVo;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目查询方法抽象
 * @author: 阿宽不是程序员
 **/
public interface SelectPageHandle {
    
    PageVo<ProgramListVo> selectPage(ProgramPageListDto programPageListDto);
    
    String getType();
}
