package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.dto.ProgramListDto;
import com.example.dto.ProgramPageListDto;
import com.example.entity.Program;

import java.util.List;

/**
 * <p>
 * 节目表 Mapper 接口
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
public interface ProgramMapper extends BaseMapper<Program> {
    
    List<Program> selectHomeList(ProgramListDto programListDto);
    
    IPage<Program> selectPage(IPage<Program> page, ProgramPageListDto programPageListDto);
}
