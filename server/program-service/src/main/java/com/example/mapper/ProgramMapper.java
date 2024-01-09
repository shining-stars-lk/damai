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

//    @Select({
//        "<script>",    
//            "select",
//                "dp.id,dp.area_id,dp.program_category_id,dp.title,dp.actor,dp.item_picture,",
//                "ds.show_time,ds.show_week_time,ds.show_day_time",
//            "from d_program dp left join d_program_show_time ds",
//            "where dp.status = 1 and ds.status = 1",
//            "<if test ='areaId != null'> and dp.area_id = #{logicNo,jdbcType=BIGINT} </if>",
//            "<if test = 'programCategoryIds != null and programCategoryIds.size>0'>",
//            " and dp.program_category_id in",
//            " <foreach collection=\"programCategoryIds\" item=\"programCategoryId\" index=\"index\" open=\"(\" close=\")\" separator=\",\">",
//            "        #{programCategoryId,jdbcType=BIGINT}",
//            " </foreach>",
//            "</if>",
//            "<if test = 'programCategoryIds == null or programCategoryIds.size == 0'>",
//            " and 1!=1",
//            "</if>",
//            "<if test = 'showTime != null'> and ds.show_day_time = #{showTime}</if>",
//            "<if test = 'time != null'> and ds.show_day_time <![CDATA[ <= ]]> #{time}</if>",
//        "</script>",    
//            
//    })
    IPage<Program> selectPage(IPage<Program> page, ProgramPageListDto programPageListDto);
}
