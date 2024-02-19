package com.damai.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.damai.dto.ProgramPageListDto;
import com.damai.entity.Program;
import com.damai.entity.ProgramV2;
import org.apache.ibatis.annotations.Param;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目 mapper
 * @author: 阿宽不是程序员
 **/
public interface ProgramMapper extends BaseMapper<Program> {
    
    IPage<ProgramV2> selectPage(IPage<ProgramV2> page, @Param("programPageListDto")ProgramPageListDto programPageListDto);
}
