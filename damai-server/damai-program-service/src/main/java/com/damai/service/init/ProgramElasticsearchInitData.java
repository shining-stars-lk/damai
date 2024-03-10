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
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目es缓存操作
 * @author: 阿宽不是程序员
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
    
    /**
     * 项目启动后，异步将program的数据更新到Elasticsearch中，当数据量特别大时，生产环境绝对不会这么做
     * 会每个一个节目到数据库后，就会添加到Elasticsearch中，以及用定时任务来更新到Elasticsearch中
     * */
    @Override
    public void executeInit(final ConfigurableApplicationContext context) {
        BusinessThreadPool.execute(this::initElasticsearchData);
    }
    
    public boolean indexAdd(){
        boolean result = businessEsHandle.checkIndex(SpringUtil.getPrefixDistinctionName() + "-" + 
                ProgramDocumentParamName.INDEX_NAME, ProgramDocumentParamName.INDEX_TYPE);
        if (result) {
            return false;
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
        
        EsDocumentMappingDto idDto = new EsDocumentMappingDto();
        idDto.setParamName(ProgramDocumentParamName.ID);
        idDto.setParamType("long");
        list.add(idDto);
        
        EsDocumentMappingDto titleDto = new EsDocumentMappingDto();
        titleDto.setParamName(ProgramDocumentParamName.TITLE);
        titleDto.setParamType("text");
        list.add(titleDto);
        
        EsDocumentMappingDto actorDto = new EsDocumentMappingDto();
        actorDto.setParamName(ProgramDocumentParamName.ACTOR);
        actorDto.setParamType("text");
        list.add(actorDto);
        
        EsDocumentMappingDto placeDto = new EsDocumentMappingDto();
        placeDto.setParamName(ProgramDocumentParamName.PLACE);
        placeDto.setParamType("text");
        list.add(placeDto);
        
        EsDocumentMappingDto itemPictureDto = new EsDocumentMappingDto();
        itemPictureDto.setParamName(ProgramDocumentParamName.ITEM_PICTURE);
        itemPictureDto.setParamType("text");
        list.add(itemPictureDto);
        
        EsDocumentMappingDto areaIdDto = new EsDocumentMappingDto();
        areaIdDto.setParamName(ProgramDocumentParamName.AREA_ID);
        areaIdDto.setParamType("long");
        list.add(areaIdDto);
        
        EsDocumentMappingDto areaNameDto = new EsDocumentMappingDto();
        areaNameDto.setParamName(ProgramDocumentParamName.AREA_NAME);
        areaNameDto.setParamType("text");
        list.add(areaNameDto);
        
        EsDocumentMappingDto programCategoryIdDto = new EsDocumentMappingDto();
        programCategoryIdDto.setParamName(ProgramDocumentParamName.PROGRAM_CATEGORY_ID);
        programCategoryIdDto.setParamType("long");
        list.add(programCategoryIdDto);
        
        EsDocumentMappingDto programCategoryNameDto = new EsDocumentMappingDto();
        programCategoryNameDto.setParamName(ProgramDocumentParamName.PROGRAM_CATEGORY_NAME);
        programCategoryNameDto.setParamType("text");
        list.add(programCategoryNameDto);
        
        EsDocumentMappingDto parentProgramCategoryIdDto = new EsDocumentMappingDto();
        parentProgramCategoryIdDto.setParamName(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_ID);
        parentProgramCategoryIdDto.setParamType("long");
        list.add(parentProgramCategoryIdDto);
        
        EsDocumentMappingDto parentProgramCategoryNameDto = new EsDocumentMappingDto();
        parentProgramCategoryNameDto.setParamName(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_NAME);
        parentProgramCategoryNameDto.setParamType("text");
        list.add(parentProgramCategoryNameDto);
        
        EsDocumentMappingDto showTimeDto = new EsDocumentMappingDto();
        showTimeDto.setParamName(ProgramDocumentParamName.SHOW_TIME);
        showTimeDto.setParamType("date");
        list.add(showTimeDto);
        
        EsDocumentMappingDto showDayTimeDto = new EsDocumentMappingDto();
        showDayTimeDto.setParamName(ProgramDocumentParamName.SHOW_DAY_TIME);
        showDayTimeDto.setParamType("date");
        list.add(showDayTimeDto);
        
        EsDocumentMappingDto showWeekTimeDto = new EsDocumentMappingDto();
        showWeekTimeDto.setParamName(ProgramDocumentParamName.SHOW_WEEK_TIME);
        showWeekTimeDto.setParamType("text");
        list.add(showWeekTimeDto);
        
        EsDocumentMappingDto minPriceDto = new EsDocumentMappingDto();
        minPriceDto.setParamName(ProgramDocumentParamName.MIN_PRICE);
        minPriceDto.setParamType("integer");
        list.add(minPriceDto);
        
        EsDocumentMappingDto maxPriceDto = new EsDocumentMappingDto();
        maxPriceDto.setParamName(ProgramDocumentParamName.MAX_PRICE);
        maxPriceDto.setParamType("integer");
        list.add(maxPriceDto);
        
        return list;
    }
    
    public void initElasticsearchData(){
        if (!indexAdd()) {
            return;
        }
        List<Long> allProgramIdList = programService.getAllProgramIdList();
        //根据节目id统计出票档的最低价和最高价的集合map, key：节目id，value：票档
        Map<Long, TicketCategoryAggregate> ticketCategorieMap = programService.selectTicketCategorieMap(allProgramIdList);
        
        for (Long programId : allProgramIdList) {
            ProgramVo programVo = programService.getDetailFromDb(programId);
            Map<String,Object> map = new HashMap<>(32);
            map.put(ProgramDocumentParamName.ID,programVo.getId());
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
            map.put(ProgramDocumentParamName.SHOW_TIME, programVo.getShowTime());
            map.put(ProgramDocumentParamName.SHOW_DAY_TIME,programVo.getShowDayTime());
            map.put(ProgramDocumentParamName.SHOW_WEEK_TIME,programVo.getShowWeekTime());
            //最低价
            map.put(ProgramDocumentParamName.MIN_PRICE,Optional.ofNullable(ticketCategorieMap.get(programVo.getId())).map(TicketCategoryAggregate::getMinPrice).orElse(null));
            //最高价
            map.put(ProgramDocumentParamName.MAX_PRICE,Optional.ofNullable(ticketCategorieMap.get(programVo.getId())).map(TicketCategoryAggregate::getMaxPrice).orElse(null));
            businessEsHandle.add(SpringUtil.getPrefixDistinctionName() + "-" + 
                    ProgramDocumentParamName.INDEX_NAME, ProgramDocumentParamName.INDEX_TYPE,map);
        }
    }
}
