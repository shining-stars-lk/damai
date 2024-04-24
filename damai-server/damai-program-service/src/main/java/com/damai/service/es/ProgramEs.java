package com.damai.service.es;

import com.damai.core.SpringUtil;
import com.damai.dto.EsDataQueryDto;
import com.damai.dto.ProgramListDto;
import com.damai.dto.ProgramPageListDto;
import com.damai.dto.ProgramSearchDto;
import com.damai.page.PageUtil;
import com.damai.page.PageVo;
import com.damai.service.init.ProgramDocumentParamName;
import com.damai.util.BusinessEsHandle;
import com.damai.util.StringUtil;
import com.damai.vo.ProgramHomeVo;
import com.damai.vo.ProgramListVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: es查询
 * @author: 阿宽不是程序员
 **/
@Slf4j
@Component
public class ProgramEs {
    
    @Autowired
    private BusinessEsHandle businessEsHandle;
    
    public List<ProgramHomeVo> selectHomeList(ProgramListDto programPageListDto) {
        List<ProgramHomeVo> programHomeVoList = new ArrayList<>();
        
        try {
            List<EsDataQueryDto> programEsQueryDto = new ArrayList<>();
            EsDataQueryDto areaIdQueryDto = new EsDataQueryDto();
            areaIdQueryDto.setParamName(ProgramDocumentParamName.AREA_ID);
            areaIdQueryDto.setParamValue(programPageListDto.getAreaId());
            programEsQueryDto.add(areaIdQueryDto);
            EsDataQueryDto parentProgramCategoryIdQueryDto = new EsDataQueryDto();
            parentProgramCategoryIdQueryDto.setParamName(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_ID);
            parentProgramCategoryIdQueryDto.setParamValue(programPageListDto.getParentProgramCategoryIds());
            programEsQueryDto.add(parentProgramCategoryIdQueryDto);
            PageInfo<ProgramListVo> pageInfo = businessEsHandle.queryPage(
                    SpringUtil.getPrefixDistinctionName() + "-" + ProgramDocumentParamName.INDEX_NAME,
                    ProgramDocumentParamName.INDEX_TYPE, programEsQueryDto, 1, 7, ProgramListVo.class);
            if (!pageInfo.getList().isEmpty()) {
                String areaName = pageInfo.getList().get(0).getAreaName();
                ProgramHomeVo programHomeVo = new ProgramHomeVo();
                programHomeVo.setCategoryName(areaName);
                programHomeVo.setProgramListVoList(pageInfo.getList());
                programHomeVoList.add(programHomeVo);
            }
        }catch (Exception e) {
            log.error("businessEsHandle.queryPage error",e);
        }
        return programHomeVoList;
    }
    
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
            if (Objects.nonNull(programPageListDto.getParentProgramCategoryId())) {
                EsDataQueryDto parentProgramCategoryIdQueryDto = new EsDataQueryDto();
                parentProgramCategoryIdQueryDto.setParamName(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_ID);
                parentProgramCategoryIdQueryDto.setParamValue(programPageListDto.getParentProgramCategoryId());
                esDataQueryDtoList.add(parentProgramCategoryIdQueryDto);
            }
            if (Objects.nonNull(programPageListDto.getProgramCategoryId())) {
                EsDataQueryDto programCategoryIdQueryDto = new EsDataQueryDto();
                programCategoryIdQueryDto.setParamName(ProgramDocumentParamName.PROGRAM_CATEGORY_ID);
                programCategoryIdQueryDto.setParamValue(programPageListDto.getProgramCategoryId());
                esDataQueryDtoList.add(programCategoryIdQueryDto);
            }
            if (Objects.nonNull(programPageListDto.getStartDateTime()) && 
                    Objects.nonNull(programPageListDto.getEndDateTime())) {
                EsDataQueryDto showDayTimeQueryDto = new EsDataQueryDto();
                showDayTimeQueryDto.setParamName(ProgramDocumentParamName.SHOW_DAY_TIME);
                showDayTimeQueryDto.setStartTime(programPageListDto.getStartDateTime());
                showDayTimeQueryDto.setEndTime(programPageListDto.getEndDateTime());
                esDataQueryDtoList.add(showDayTimeQueryDto);
            }
            
            PageInfo<ProgramListVo> programListVoPageInfo = businessEsHandle.queryPage(
                    SpringUtil.getPrefixDistinctionName() + "-" + ProgramDocumentParamName.INDEX_NAME,
                    ProgramDocumentParamName.INDEX_TYPE, esDataQueryDtoList, programPageListDto.getPageNumber(),
                    programPageListDto.getPageSize(), ProgramListVo.class);
            pageVo = PageUtil.convertPage(programListVoPageInfo, programListVo -> programListVo);
        }catch (Exception e) {
            log.error("selectPage error",e);
        }
        return pageVo;
    }
    
    public PageVo<ProgramListVo> search(ProgramSearchDto programSearchDto) {
        PageVo<ProgramListVo> pageVo = new PageVo<>();
        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (Objects.nonNull(programSearchDto.getAreaId())) {
                QueryBuilder builds = QueryBuilders.termQuery(ProgramDocumentParamName.AREA_ID, programSearchDto.getAreaId());
                boolQuery.must(builds);
            }
            if (Objects.nonNull(programSearchDto.getParentProgramCategoryId())) {
                QueryBuilder builds = QueryBuilders.termQuery(ProgramDocumentParamName.PARENT_PROGRAM_CATEGORY_ID, programSearchDto.getParentProgramCategoryId());
                boolQuery.must(builds);
            }
            if (Objects.nonNull(programSearchDto.getStartDateTime()) &&
                    Objects.nonNull(programSearchDto.getEndDateTime())) {
                QueryBuilder builds = QueryBuilders.rangeQuery(ProgramDocumentParamName.SHOW_DAY_TIME)
                        .from(programSearchDto.getStartDateTime()).to(programSearchDto.getEndDateTime()).includeLower(true);
                boolQuery.must(builds);
            }
            if (StringUtil.isNotEmpty(programSearchDto.getContent())) {
                BoolQueryBuilder innerBoolQuery = QueryBuilders.boolQuery();
                innerBoolQuery.should(QueryBuilders.matchQuery(ProgramDocumentParamName.TITLE, programSearchDto.getContent()));
                innerBoolQuery.should(QueryBuilders.matchQuery(ProgramDocumentParamName.ACTOR, programSearchDto.getContent()));
                innerBoolQuery.minimumShouldMatch(1);
                boolQuery.must(innerBoolQuery);
            }
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(boolQuery);
            searchSourceBuilder.trackTotalHits(true);
            searchSourceBuilder.from((programSearchDto.getPageNumber() - 1) * programSearchDto.getPageSize());
            searchSourceBuilder.size(programSearchDto.getPageSize());
            searchSourceBuilder.highlighter(getHighlightBuilder(Arrays.asList(ProgramDocumentParamName.TITLE,
                    ProgramDocumentParamName.ACTOR)));
            List<ProgramListVo> list = new ArrayList<>();
            PageInfo<ProgramListVo> pageInfo = new PageInfo<>(list);
            pageInfo.setPageNum(programSearchDto.getPageNumber());
            pageInfo.setPageSize(programSearchDto.getPageSize());
            businessEsHandle.executeQuery(SpringUtil.getPrefixDistinctionName() + "-" + ProgramDocumentParamName.INDEX_NAME,
                    ProgramDocumentParamName.INDEX_TYPE,list,pageInfo,ProgramListVo.class,
                    searchSourceBuilder,Arrays.asList(ProgramDocumentParamName.TITLE,ProgramDocumentParamName.ACTOR));
            pageVo = PageUtil.convertPage(pageInfo,programListVo -> programListVo);
        }catch (Exception e) {
            log.error("search error",e);
        }
        return pageVo;
    }
    
    public HighlightBuilder getHighlightBuilder(List<String> fieldNameList){
        // 创建一个HighlightBuilder
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String fieldName : fieldNameList) {
            // 为特定字段添加高亮设置
            HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(fieldName);
            highlightTitle.preTags("<em>");
            highlightTitle.postTags("</em>");
            highlightBuilder.field(highlightTitle);
        }
        return highlightBuilder;
    }
}
