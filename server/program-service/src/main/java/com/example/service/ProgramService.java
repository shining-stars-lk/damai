package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.client.BaseDataClient;
import com.example.common.ApiResponse;
import com.example.dto.AreaGetDto;
import com.example.dto.AreaSelectDto;
import com.example.dto.ProgramGetDto;
import com.example.dto.ProgramListDto;
import com.example.dto.ProgramPageListDto;
import com.example.entity.Program;
import com.example.entity.ProgramCategory;
import com.example.entity.ProgramShowTime;
import com.example.entity.ProgramV2;
import com.example.entity.TicketCategory;
import com.example.entity.TicketCategoryAggregate;
import com.example.mapper.ProgramCategoryMapper;
import com.example.mapper.ProgramMapper;
import com.example.mapper.ProgramShowTimeMapper;
import com.example.mapper.TicketCategoryMapper;
import com.example.util.PageUtil;
import com.example.vo.AreaVo;
import com.example.vo.ProgramListVo;
import com.example.vo.ProgramVo;
import com.example.vo.TicketCategoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 节目表 服务实现类
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@Slf4j
@Service
public class ProgramService extends ServiceImpl<ProgramMapper, Program> {
    
    @Autowired
    private ProgramMapper programMapper;
    
    @Autowired
    private ProgramShowTimeMapper programShowTimeMapper;
    
    @Autowired
    private ProgramCategoryMapper programCategoryMapper; 
    
    @Autowired
    private TicketCategoryMapper ticketCategoryMapper;
    
    @Autowired
    private BaseDataClient baseDataClient;
    
    public Map<String,List<ProgramListVo>> selectHomeList(ProgramListDto programPageListDto) {
        Map<String,List<ProgramListVo>> programListVoMap = new HashMap<>();
        
        LambdaQueryWrapper<Program> programLambdaQueryWrapper = Wrappers.lambdaQuery(Program.class)
                .eq(Program::getAreaId,programPageListDto.getAreaId())
                .in(Program::getParentProgramCategoryId, programPageListDto.getParentProgramCategoryIds());
        List<Program> programList = programMapper.selectList(programLambdaQueryWrapper);
        
        List<Long> programIdList = programList.stream().map(Program::getId).collect(Collectors.toList());
        
        LambdaQueryWrapper<ProgramShowTime> programShowTimeLambdaQueryWrapper = Wrappers.lambdaQuery(ProgramShowTime.class)
                .in(ProgramShowTime::getProgramId, programIdList);
        List<ProgramShowTime> programShowTimeList = programShowTimeMapper.selectList(programShowTimeLambdaQueryWrapper);
        Map<Long, List<ProgramShowTime>> ProgramShowTimeMap = programShowTimeList.stream().collect(Collectors.groupingBy(ProgramShowTime::getProgramId));
        
        LambdaQueryWrapper<ProgramCategory> pcLambdaQueryWrapper = Wrappers.lambdaQuery(ProgramCategory.class)
                .in(ProgramCategory::getId, programList.stream().map(Program::getParentProgramCategoryId).collect(Collectors.toList()));
        List<ProgramCategory> programCategorieList = programCategoryMapper.selectList(pcLambdaQueryWrapper);
        Map<Long, String> programCategorieMap = programCategorieList.stream()
                .collect(Collectors.toMap(ProgramCategory::getId, ProgramCategory::getName, (v1, v2) -> v2));
        
        List<TicketCategoryAggregate> ticketCategorieList = ticketCategoryMapper.selectAggregateList(programIdList);
        Map<Long, TicketCategoryAggregate> ticketCategorieMap = ticketCategorieList
                .stream()
                .collect(Collectors.toMap(TicketCategoryAggregate::getProgramId, ticketCategory -> ticketCategory, (v1, v2) -> v2));
        
        Map<Long, List<Program>> programMap = programList.stream().collect(Collectors.groupingBy(Program::getParentProgramCategoryId));
        for (Entry<Long, List<Program>> programEntry : programMap.entrySet()) {
            Long key = programEntry.getKey();
            List<Program> value = programEntry.getValue();
            List<ProgramListVo> programListVoList = new ArrayList<>();
            for (Program program : value) {
                ProgramListVo programListVo = new ProgramListVo();
                BeanUtil.copyProperties(program,programListVo);
                programListVo.setShowTime(Optional.ofNullable(ProgramShowTimeMap.get(program.getId()))
                        .filter(list -> list.size() > 0)
                        .map(list -> list.get(0))
                        .map(ProgramShowTime::getShowTime)
                        .orElse(null));
                programListVo.setShowDayTime(Optional.ofNullable(ProgramShowTimeMap.get(program.getId()))
                        .filter(list -> list.size() > 0)
                        .map(list -> list.get(0))
                        .map(ProgramShowTime::getShowDayTime)
                        .orElse(null));
                programListVo.setShowWeekTime(Optional.ofNullable(ProgramShowTimeMap.get(program.getId()))
                        .filter(list -> list.size() > 0)
                        .map(list -> list.get(0))
                        .map(ProgramShowTime::getShowWeekTime)
                        .orElse(null));
                programListVo.setMaxPrice(Optional.ofNullable(ticketCategorieMap.get(program.getId())).map(TicketCategoryAggregate::getMaxPrice).orElse(null));
                programListVo.setMinPrice(Optional.ofNullable(ticketCategorieMap.get(program.getId())).map(TicketCategoryAggregate::getMinPrice).orElse(null));
                programListVoList.add(programListVo);
            }
            programListVoMap.put(programCategorieMap.get(key),programListVoList);
        }
        return programListVoMap;
    }
    public IPage<ProgramListVo> selectPage(ProgramPageListDto programPageListDto) {
        
        IPage<ProgramV2> iPage = programMapper.selectPage(PageUtil.getPageParams(programPageListDto), programPageListDto);
        
        Set<Long> programCategoryIdList = iPage.getRecords().stream().map(Program::getProgramCategoryId).collect(Collectors.toSet());
        LambdaQueryWrapper<ProgramCategory> pcLambdaQueryWrapper = Wrappers.lambdaQuery(ProgramCategory.class)
                .in(ProgramCategory::getId, programCategoryIdList);
        List<ProgramCategory> programCategoryList = programCategoryMapper.selectList(pcLambdaQueryWrapper);
        Map<Long, String> programCategoryMap = programCategoryList
                .stream()
                .collect(Collectors.toMap(ProgramCategory::getId, ProgramCategory::getName, (v1, v2) -> v2));
        
        
        List<Long> programIdList = iPage.getRecords().stream().map(Program::getId).collect(Collectors.toList());
        List<TicketCategoryAggregate> ticketCategorieList = ticketCategoryMapper.selectAggregateList(programIdList);
        Map<Long, TicketCategoryAggregate> ticketCategorieMap = ticketCategorieList
                .stream()
                .collect(Collectors.toMap(TicketCategoryAggregate::getProgramId, ticketCategory -> ticketCategory, (v1, v2) -> v2));
        
        Map<Long,String> areaMap = new HashMap<>();
        AreaSelectDto areaSelectDto = new AreaSelectDto();
        areaSelectDto.setIdList(iPage.getRecords().stream().map(Program::getAreaId).distinct().collect(Collectors.toList()));
        ApiResponse<List<AreaVo>> areaResponse = baseDataClient.selectByIdList(areaSelectDto);
        if (Objects.equals(areaResponse.getCode(), ApiResponse.ok().getCode())) {
            if (CollectionUtil.isNotEmpty(areaResponse.getData())) {
                areaMap = areaResponse.getData().stream().collect(Collectors.toMap(AreaVo::getId,AreaVo::getName,(v1,v2) -> v2));
            }
        }else {
            log.error("base-data selectByIdList rpc error areaResponse:{}", JSON.toJSONString(areaResponse));
        }
        List<ProgramListVo> programListVoList = new ArrayList<>();
        
        for (final ProgramV2 programV2 : iPage.getRecords()) {
            ProgramListVo programListVo = new ProgramListVo();
            BeanUtil.copyProperties(programV2, programListVo);
            programListVo.setAreaName(areaMap.get(programV2.getAreaId()));
            programListVo.setProgramCategoryName(programCategoryMap.get(programV2.getProgramCategoryId()));
            programListVo.setMinPrice(Optional.ofNullable(ticketCategorieMap.get(programV2.getId())).map(TicketCategoryAggregate::getMinPrice).orElse(null));
            programListVo.setMaxPrice(Optional.ofNullable(ticketCategorieMap.get(programV2.getId())).map(TicketCategoryAggregate::getMaxPrice).orElse(null));
            programListVoList.add(programListVo);
        }
        return PageUtil.convertPage(iPage,programListVoList);
    }
    
    public ProgramVo getById(ProgramGetDto programGetDto) {
        ProgramVo programVo = new ProgramVo();
        Program program = programMapper.selectById(programGetDto.getId());
        if (Objects.isNull(program)) {
            return programVo;
        }
        BeanUtil.copyProperties(program,programVo);
        
        ProgramCategory programCategory = programCategoryMapper.selectById(program.getProgramCategoryId());
        if (Objects.nonNull(programCategory)) {
            programVo.setProgramCategoryName(programCategory.getName());
        }
        
        LambdaQueryWrapper<ProgramShowTime> programShowTimeLambdaQueryWrapper = Wrappers.lambdaQuery(ProgramShowTime.class)
                .eq(ProgramShowTime::getProgramId, program.getId());
        ProgramShowTime programShowTime = programShowTimeMapper.selectOne(programShowTimeLambdaQueryWrapper);
        if (Objects.nonNull(programShowTime)) {
            programVo.setShowTime(programShowTime.getShowTime());
            programVo.setShowDayTime(programShowTime.getShowDayTime());
            programVo.setShowWeekTime(programShowTime.getShowWeekTime());
        }
        
        AreaGetDto areaGetDto = new AreaGetDto();
        areaGetDto.setId(program.getAreaId());
        ApiResponse<AreaVo> areaResponse = baseDataClient.getById(areaGetDto);
        if (Objects.equals(areaResponse.getCode(), ApiResponse.ok().getCode())) {
            if (Objects.nonNull(areaResponse.getData())) {
                programVo.setAreaName(areaResponse.getData().getName());
            }
        }else {
            log.error("base-data rpc getById error areaResponse:{}", JSON.toJSONString(areaResponse));
        }
        
        
        LambdaQueryWrapper<TicketCategory> ticketCategoryLambdaQueryWrapper = Wrappers.lambdaQuery(TicketCategory.class)
                .eq(TicketCategory::getProgramId, program.getId());
        List<TicketCategory> ticketCategoryList = ticketCategoryMapper.selectList(ticketCategoryLambdaQueryWrapper);
        List<TicketCategoryVo> ticketCategoryVoList = ticketCategoryList.stream().map(ticketCategory -> {
            TicketCategoryVo ticketCategoryVo = new TicketCategoryVo();
            BeanUtil.copyProperties(ticketCategory, ticketCategoryVo);
            return ticketCategoryVo;
        }).collect(Collectors.toList());
        programVo.setTicketCategoryVoList(ticketCategoryVoList);
        return programVo;
    }
    
    
}
