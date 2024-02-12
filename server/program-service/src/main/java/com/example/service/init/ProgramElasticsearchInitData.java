package com.example.service.init;

import com.example.BusinessThreadPool;
import com.example.dto.EsDocumentMappingDto;
import com.example.entity.TicketCategoryAggregate;
import com.example.init.InitData;
import com.example.service.ProgramService;
import com.example.util.BusinessEsHandle;
import com.example.vo.ProgramVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ProgramElasticsearchInitData implements InitData {
    
    @Autowired
    private BusinessEsHandle businessEsHandle;
    
    @Autowired
    private ProgramService programService;
    
    
    /**
     * 项目启动后，异步将program的数据更新到Elasticsearch中，当数据量特别大时，生产环境绝对不会这么做
     * 会每个一个节目到数据库后，就会添加到Elasticsearch中，以及用定时任务来更新到Elasticsearch中
     * */
    @Override
    public void init() {
        BusinessThreadPool.execute(this::initElasticsearchData);
    }
    public boolean indexAdd(){
        boolean result = businessEsHandle.checkIndex(ProgramDocumentParamName.INDEX_NAME, ProgramDocumentParamName.INDEX_TYPE);
        if (result) {
            return false;
        }
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
        
        EsDocumentMappingDto parentProgramCategoryIdDto = new EsDocumentMappingDto();
        parentProgramCategoryIdDto.setParamName(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_ID);
        parentProgramCategoryIdDto.setParamType("long");
        list.add(parentProgramCategoryIdDto);
        
        EsDocumentMappingDto programCategoryNameDto = new EsDocumentMappingDto();
        programCategoryNameDto.setParamName(ProgramDocumentParamName.PROGRAM_CATEGORY_NAME);
        programCategoryNameDto.setParamType("text");
        list.add(programCategoryNameDto);
        
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
        
        try {
            businessEsHandle.createIndex(ProgramDocumentParamName.INDEX_NAME, ProgramDocumentParamName.INDEX_TYPE,list);
            return true;
        }catch (Exception e) {
            log.error("createIndex error",e);
        }
        return false;
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
            Map<String,Object> map = new HashMap<>();
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
            map.put(ProgramDocumentParamName.SHOW_TIME, programVo.getShowTime());
            map.put(ProgramDocumentParamName.SHOW_DAY_TIME,programVo.getShowDayTime());
            map.put(ProgramDocumentParamName.SHOW_WEEK_TIME,programVo.getShowWeekTime());
            //最低价
            map.put(ProgramDocumentParamName.MIN_PRICE,Optional.ofNullable(ticketCategorieMap.get(programVo.getId())).map(TicketCategoryAggregate::getMinPrice).orElse(null));
            //最高价
            map.put(ProgramDocumentParamName.MAX_PRICE,Optional.ofNullable(ticketCategorieMap.get(programVo.getId())).map(TicketCategoryAggregate::getMaxPrice).orElse(null));
            businessEsHandle.add(ProgramDocumentParamName.INDEX_NAME, ProgramDocumentParamName.INDEX_TYPE,map);
        }
    }
}
