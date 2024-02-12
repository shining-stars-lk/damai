package com.example.service.pagestrategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.dto.EsDataQueryDto;
import com.example.dto.ProgramPageListDto;
import com.example.enums.TimeType;
import com.example.page.PageUtil;
import com.example.page.PageVo;
import com.example.service.init.ProgramDocumentParamName;
import com.example.service.pagestrategy.ProgramConstant;
import com.example.service.pagestrategy.SelectPageHandle;
import com.example.util.BusinessEsHandle;
import com.example.util.DateUtils;
import com.example.vo.ProgramListVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
public class SelectPageEsHandle implements SelectPageHandle {
    
    @Autowired
    private BusinessEsHandle businessEsHandle;
    @Override
    public PageVo<ProgramListVo> selectPage(ProgramPageListDto programPageListDto) {
        PageVo<ProgramListVo> pageVo = new PageVo<>();
        try {
            List<EsDataQueryDto> esDataQueryDtoList = new ArrayList<>();
            if (Objects.nonNull(programPageListDto.getAreaId())) {
                EsDataQueryDto areaIdQueryDto = new EsDataQueryDto();
                areaIdQueryDto.setParamName(ProgramDocumentParamName.AREA_ID);
                areaIdQueryDto.setParamValue(programPageListDto.getAreaId());
                esDataQueryDtoList.add(areaIdQueryDto);
            }
            if (CollectionUtil.isNotEmpty(programPageListDto.getProgramCategoryIds())) {
                EsDataQueryDto programCategoryIdQueryDto = new EsDataQueryDto();
                programCategoryIdQueryDto.setParamName(ProgramDocumentParamName.PROGRAM_CATEGORY_ID);
                programCategoryIdQueryDto.setParamValue(programPageListDto.getProgramCategoryIds());
                esDataQueryDtoList.add(programCategoryIdQueryDto);
            }
            if (CollectionUtil.isNotEmpty(programPageListDto.getParentProgramCategoryIds())) {
                EsDataQueryDto parentProgramCategoryIdQueryDto = new EsDataQueryDto();
                parentProgramCategoryIdQueryDto.setParamName(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_ID);
                parentProgramCategoryIdQueryDto.setParamValue(programPageListDto.getParentProgramCategoryIds());
                esDataQueryDtoList.add(parentProgramCategoryIdQueryDto);
            }
            if (Objects.nonNull(programPageListDto.getShowDayTime())) {
                EsDataQueryDto showDayTimeQueryDto = new EsDataQueryDto();
                showDayTimeQueryDto.setParamName(ProgramDocumentParamName.SHOW_DAY_TIME);
                showDayTimeQueryDto.setParamValue(programPageListDto.getShowDayTime());
                esDataQueryDtoList.add(showDayTimeQueryDto);
            }
            if (Objects.nonNull(programPageListDto.getTimeType())) {
                Date time = null;
                if (programPageListDto.getTimeType().equals(TimeType.WEEK.getCode())) {
                    time = DateUtils.addWeek(DateUtils.now(),1);
                }else if (programPageListDto.getTimeType().equals(TimeType.MONTH.getCode())) {
                    time = DateUtils.addMonth(DateUtils.now(),1);
                }
                EsDataQueryDto showDayTimeQueryDto = new EsDataQueryDto();
                showDayTimeQueryDto.setParamName(ProgramDocumentParamName.SHOW_DAY_TIME);
                showDayTimeQueryDto.setStartTime(DateUtils.now());
                showDayTimeQueryDto.setEndTime(time);
                esDataQueryDtoList.add(showDayTimeQueryDto);
            }
            
            PageInfo<ProgramListVo> programListVoPageInfo = businessEsHandle.queryPage(ProgramDocumentParamName.INDEX_NAME,
                    ProgramDocumentParamName.INDEX_TYPE, esDataQueryDtoList, programPageListDto.getPageNumber(),
                    programPageListDto.getPageSize(), ProgramListVo.class);
            pageVo = PageUtil.convertPage(programListVoPageInfo, programListVo -> programListVo);
        }catch (Exception e) {
            log.error("selectPage error",e);
        }
        return pageVo;
    }
    
    @Override
    public String getType() {
        return ProgramConstant.ES_TYPE_NAME;
    }
}
