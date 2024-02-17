package com.damai.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.damai.dto.ProgramPageListDto;
import com.damai.entity.Program;
import com.damai.entity.ProgramV2;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 节目表 Mapper 接口
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
public interface ProgramMapper extends BaseMapper<Program> {
    
    IPage<ProgramV2> selectPage(IPage<ProgramV2> page, @Param("programPageListDto")ProgramPageListDto programPageListDto);
}
