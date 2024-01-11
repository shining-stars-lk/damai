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
import com.example.dto.AreaSelectDto;
import com.example.dto.ProgramGetDto;
import com.example.dto.ProgramListDto;
import com.example.dto.ProgramPageListDto;
import com.example.entity.Program;
import com.example.entity.ProgramCategory;
import com.example.entity.ProgramShowTime;
import com.example.entity.TicketCategory;
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
        List<Program> programList = programMapper.selectHomeList(programPageListDto);
        LambdaQueryWrapper<ProgramCategory> pcLambdaQueryWrapper = Wrappers.lambdaQuery(ProgramCategory.class)
                .in(ProgramCategory::getId, programList.stream().map(Program::getParentProgramCategoryId).collect(Collectors.toList()));
        List<ProgramCategory> programCategorieList = programCategoryMapper.selectList(pcLambdaQueryWrapper);
        Map<Long, String> programCategorieMap = programCategorieList.stream()
                .collect(Collectors.toMap(ProgramCategory::getId, ProgramCategory::getName, (v1, v2) -> v2));
        Map<Long, List<Program>> programMap = programList.stream().collect(Collectors.groupingBy(Program::getParentProgramCategoryId));
        
        
        
        LambdaQueryWrapper<TicketCategory> tcLambdaQueryWrapper = Wrappers.query(TicketCategory.class)
                .select("program_id,min(price) as min_price,max(price) as max_price")
                .lambda()
                .in(TicketCategory::getProgramId, programList.stream().map(Program::getId).collect(Collectors.toList()))
                .groupBy(TicketCategory::getProgramId);
        List<TicketCategory> ticketCategorieList = ticketCategoryMapper.selectList(tcLambdaQueryWrapper);
        Map<Long, TicketCategory> ticketCategorieMap = ticketCategorieList
                .stream()
                .collect(Collectors.toMap(TicketCategory::getProgramId, ticketCategory -> ticketCategory, (v1, v2) -> v2));
        
        for (Entry<Long, List<Program>> programEntry : programMap.entrySet()) {
            Long key = programEntry.getKey();
            List<Program> value = programEntry.getValue();
            List<ProgramListVo> programListVoList = new ArrayList<>();
            for (Program program : value) {
                ProgramListVo programListVo = new ProgramListVo();
                BeanUtil.copyProperties(program,programListVo);
                programListVo.setMaxPrice(Optional.ofNullable(ticketCategorieMap.get(program.getId())).map(TicketCategory::getMaxPrice).orElse(null));
                programListVo.setMinPrice(Optional.ofNullable(ticketCategorieMap.get(program.getId())).map(TicketCategory::getMinPrice).orElse(null));
                programListVoList.add(programListVo);
            }
            programListVoMap.put(programCategorieMap.get(key),programListVoList);
        }
        return programListVoMap;
    }
    public IPage<ProgramListVo> selectPage(ProgramPageListDto programPageListDto) {
        
        IPage<Program> iPage = programMapper.selectPage(PageUtil.getPageParams(programPageListDto), programPageListDto);
        
        List<Long> programCategoryIdList = iPage.getRecords().stream().map(Program::getProgramCategoryId).collect(Collectors.toList());
        LambdaQueryWrapper<ProgramCategory> pcLambdaQueryWrapper = Wrappers.lambdaQuery(ProgramCategory.class)
                .in(ProgramCategory::getId, programCategoryIdList);
        List<ProgramCategory> programCategoryList = programCategoryMapper.selectList(pcLambdaQueryWrapper);
        Map<Long, String> programCategoryMap = programCategoryList
                .stream()
                .collect(Collectors.toMap(ProgramCategory::getId, ProgramCategory::getName, (v1, v2) -> v2));
        
        
        List<Long> programIdList = iPage.getRecords().stream().map(Program::getId).collect(Collectors.toList());
        LambdaQueryWrapper<TicketCategory> tcLambdaQueryWrapper = Wrappers.query(TicketCategory.class)
                .select("program_id,min(price) as min_price,max(price) as max_price")
                .lambda()
                .in(TicketCategory::getProgramId, programIdList)
                .groupBy(TicketCategory::getProgramId);
        List<TicketCategory> ticketCategorieList = ticketCategoryMapper.selectList(tcLambdaQueryWrapper);
        Map<Long, TicketCategory> ticketCategorieMap = ticketCategorieList
                .stream()
                .collect(Collectors.toMap(TicketCategory::getProgramId, ticketCategory -> ticketCategory, (v1, v2) -> v2));
        
        Map<Long,String> areaMap = new HashMap<>();
        AreaSelectDto areaSelectDto = new AreaSelectDto();
        areaSelectDto.setIdList(iPage.getRecords().stream().map(Program::getAreaId).collect(Collectors.toList()));
        ApiResponse<List<AreaVo>> areaResponse = baseDataClient.selectByIdList(areaSelectDto);
        if (Objects.equals(areaResponse.getCode(), ApiResponse.ok().getCode())) {
            if (CollectionUtil.isNotEmpty(areaResponse.getData())) {
                areaMap = areaResponse.getData().stream().collect(Collectors.toMap(AreaVo::getId,AreaVo::getName,(v1,v2) -> v2));
            }
        }else {
            log.error("base-data rpc error areaResponse:{}", JSON.toJSONString(areaResponse));
        }
        List<ProgramListVo> programListVoList = new ArrayList<>();
        
        for (final Program program : iPage.getRecords()) {
            ProgramListVo programListVo = new ProgramListVo();
            BeanUtil.copyProperties(program, programListVo);
            programListVo.setAreaName(areaMap.get(program.getAreaId()));
            programListVo.setProgramCategoryName(programCategoryMap.get(program.getProgramCategoryId()));
            programListVo.setMinPrice(Optional.ofNullable(ticketCategorieMap.get(program.getId())).map(TicketCategory::getMinPrice).orElse(null));
            programListVo.setMaxPrice(Optional.ofNullable(ticketCategorieMap.get(program.getId())).map(TicketCategory::getMaxPrice).orElse(null));
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
