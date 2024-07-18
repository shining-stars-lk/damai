package com.damai.service.init;

import com.damai.BusinessThreadPool;
import com.damai.core.SpringUtil;
import com.damai.dto.EsDocumentMappingDto;
import com.damai.entity.TicketCategoryAggregate;
import com.damai.initialize.base.AbstractApplicationPostConstructHandler;
import com.damai.service.ProgramService;
import com.damai.util.BusinessEsHandle;
import com.damai.vo.ProgramVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目es缓存操作
 * @author: 阿星不是程序员
 **/
@Slf4j
@Component
public class ProgramElasticsearchInitData extends AbstractApplicationPostConstructHandler {
    
    @Autowired
    private BusinessEsHandle businessEsHandle;
    
    @Autowired
    private ProgramService programService;
    
    
    @Override
    public Integer executeOrder() {
        return 3;
    }
    
    @Override
    public void executeInit(final ConfigurableApplicationContext context) {
        BusinessThreadPool.execute(() -> {
            try {
                initElasticsearchData();
            }catch (Exception e) {
                log.error("executeInit error",e);
            }
        });
    }
    
    public void initElasticsearchData(){
        if (!indexAdd()) {
            return;
        }
        List<Long> allProgramIdList = programService.getAllProgramIdList();
        Map<Long, TicketCategoryAggregate> ticketCategorieMap = programService.selectTicketCategorieMap(allProgramIdList);
        
        for (Long programId : allProgramIdList) {
            ProgramVo programVo = programService.getDetailFromDb(programId);
            Map<String,Object> map = new HashMap<>(32);
            map.put(ProgramDocumentParamName.ID,programVo.getId());
            map.put(ProgramDocumentParamName.PROGRAM_GROUP_ID,programVo.getProgramGroupId());
            map.put(ProgramDocumentParamName.PRIME,programVo.getPrime());
            map.put(ProgramDocumentParamName.TITLE,programVo.getTitle());
            map.put(ProgramDocumentParamName.ACTOR,programVo.getActor());
            map.put(ProgramDocumentParamName.PLACE,programVo.getPlace());
            map.put(ProgramDocumentParamName.ITEM_PICTURE,programVo.getItemPicture());
            map.put(ProgramDocumentParamName.AREA_ID,programVo.getAreaId());
            map.put(ProgramDocumentParamName.AREA_NAME,programVo.getAreaName());
            map.put(ProgramDocumentParamName.PROGRAM_CATEGORY_ID,programVo.getProgramCategoryId());
            map.put(ProgramDocumentParamName.PROGRAM_CATEGORY_NAME,programVo.getProgramCategoryName());
            map.put(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_ID,programVo.getParentProgramCategoryId());
            map.put(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_NAME,programVo.getParentProgramCategoryName());
            map.put(ProgramDocumentParamName.HIGH_HEAT,programVo.getHighHeat());
            map.put(ProgramDocumentParamName.ISSUE_TIME,programVo.getIssueTime());
            map.put(ProgramDocumentParamName.SHOW_TIME, programVo.getShowTime());
            map.put(ProgramDocumentParamName.SHOW_DAY_TIME,programVo.getShowDayTime());
            map.put(ProgramDocumentParamName.SHOW_WEEK_TIME,programVo.getShowWeekTime());
            map.put(ProgramDocumentParamName.MIN_PRICE,
                    Optional.ofNullable(ticketCategorieMap.get(programVo.getId()))
                            .map(TicketCategoryAggregate::getMinPrice).orElse(null));
            map.put(ProgramDocumentParamName.MAX_PRICE,
                    Optional.ofNullable(ticketCategorieMap.get(programVo.getId()))
                            .map(TicketCategoryAggregate::getMaxPrice).orElse(null));
            businessEsHandle.add(SpringUtil.getPrefixDistinctionName() + "-" + 
                    ProgramDocumentParamName.INDEX_NAME, ProgramDocumentParamName.INDEX_TYPE,map);
        }
    }
    
    public boolean indexAdd(){
        boolean result = businessEsHandle.checkIndex(SpringUtil.getPrefixDistinctionName() + "-" +
                ProgramDocumentParamName.INDEX_NAME, ProgramDocumentParamName.INDEX_TYPE);
        if (result) {
            businessEsHandle.deleteIndex(SpringUtil.getPrefixDistinctionName() + "-" +
                    ProgramDocumentParamName.INDEX_NAME);
        }
        try {
            businessEsHandle.createIndex(SpringUtil.getPrefixDistinctionName() + "-" +
                    ProgramDocumentParamName.INDEX_NAME, ProgramDocumentParamName.INDEX_TYPE,getEsMapping());
            return true;
        }catch (Exception e) {
            log.error("createIndex error",e);
        }
        return false;
    }
    
    public List<EsDocumentMappingDto> getEsMapping(){
        List<EsDocumentMappingDto> list = new ArrayList<>();
        
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.ID,"long"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.PROGRAM_GROUP_ID,"integer"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.PRIME,"long"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.TITLE,"text"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.ACTOR,"text"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.PLACE,"text"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.ITEM_PICTURE,"text"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.AREA_ID,"long"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.AREA_NAME,"text"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.PROGRAM_CATEGORY_ID,"long"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.PROGRAM_CATEGORY_NAME,"text"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_ID,"long"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_NAME,"text"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.HIGH_HEAT,"integer"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.ISSUE_TIME,"date"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.SHOW_TIME,"date"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.SHOW_DAY_TIME,"date"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.SHOW_WEEK_TIME,"text"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.MIN_PRICE,"integer"));
        list.add(new EsDocumentMappingDto(ProgramDocumentParamName.MAX_PRICE,"integer"));
        
        return list;
    }
}
