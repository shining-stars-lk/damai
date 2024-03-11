package com.damai.service.es;

import com.damai.core.SpringUtil;
import com.damai.dto.EsDataQueryDto;
import com.damai.dto.ProgramListDto;
import com.damai.service.init.ProgramDocumentParamName;
import com.damai.util.BusinessEsHandle;
import com.damai.vo.ProgramListVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: es主页查询
 * @author: 阿宽不是程序员
 **/
@Slf4j
@Component
public class SelectHomeListEs {
    
    @Autowired
    private BusinessEsHandle businessEsHandle;
    
    public Map<String, List<ProgramListVo>> selectHomeList(ProgramListDto programPageListDto) {
        Map<String,List<ProgramListVo>> programListVoMap = new HashMap<>(256);
        
        try {
            for (Long parentProgramCategoryId : programPageListDto.getParentProgramCategoryIds()) {
                List<EsDataQueryDto> programEsQueryDto = new ArrayList<>();
                EsDataQueryDto areaIdQueryDto = new EsDataQueryDto();
                areaIdQueryDto.setParamName(ProgramDocumentParamName.AREA_ID);
                areaIdQueryDto.setParamValue(programPageListDto.getAreaId());
                programEsQueryDto.add(areaIdQueryDto);
                EsDataQueryDto parentProgramCategoryIdQueryDto = new EsDataQueryDto();
                parentProgramCategoryIdQueryDto.setParamName(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_ID);
                parentProgramCategoryIdQueryDto.setParamValue(parentProgramCategoryId);
                programEsQueryDto.add(parentProgramCategoryIdQueryDto);
                PageInfo<ProgramListVo> pageInfo = businessEsHandle.queryPage(
                        SpringUtil.getPrefixDistinctionName() + "-" + ProgramDocumentParamName.INDEX_NAME,
                        ProgramDocumentParamName.INDEX_TYPE, programEsQueryDto, 1, 7, ProgramListVo.class);
                if (pageInfo.getList().size() > 0) {
                    String areaName = pageInfo.getList().get(0).getAreaName();
                    programListVoMap.put(areaName,pageInfo.getList());
                }
            }
        }catch (Exception e) {
            log.error("businessEsHandle.queryPage error",e);
        }
        return programListVoMap;
    }
}
