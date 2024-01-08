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
import com.example.dto.AreaDto;
import com.example.dto.ProgramDto;
import com.example.entity.Program;
import com.example.entity.ProgramCategory;
import com.example.entity.TicketCategory;
import com.example.mapper.ProgramCategoryMapper;
import com.example.mapper.ProgramMapper;
import com.example.mapper.TicketCategoryMapper;
import com.example.util.PageUtil;
import com.example.vo.AreaVo;
import com.example.vo.ProgramListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private ProgramCategoryMapper programCategoryMapper; 
    
    @Autowired
    private TicketCategoryMapper ticketCategoryMapper;
    
    @Autowired
    private BaseDataClient baseDataClient;
    
    public IPage<ProgramListVo> selectPage(final ProgramDto programDto) {
        
        IPage<Program> iPage = programMapper.selectPage(PageUtil.getPageParams(programDto),programDto);
        
        List<Long> programCategoryIdList = iPage.getRecords().stream().map(Program::getProgramCategoryId).collect(Collectors.toList());
        LambdaQueryWrapper<ProgramCategory> pcLambdaQueryWrapper = Wrappers.lambdaQuery(ProgramCategory.class)
                .in(ProgramCategory::getId, programCategoryIdList);
        List<ProgramCategory> programCategoryList = programCategoryMapper.selectList(pcLambdaQueryWrapper);
        Map<Long, String> programCategoryMap = programCategoryList
                .stream()
                .collect(Collectors.toMap(ProgramCategory::getId, ProgramCategory::getName, (v1, v2) -> v2));
        
        
        List<Long> programIdList = iPage.getRecords().stream().map(Program::getId).collect(Collectors.toList());
        LambdaQueryWrapper<TicketCategory> tcLambdaQueryWrapper = Wrappers.query(TicketCategory.class)
                .select("program_id,min(price) as min_price,max(price) as max_pricefrom")
                .lambda()
                .in(TicketCategory::getProgramId, programIdList)
                .groupBy(TicketCategory::getProgramId);
        List<TicketCategory> ticketCategorieList = ticketCategoryMapper.selectList(tcLambdaQueryWrapper);
        Map<Long, TicketCategory> ticketCategorieMap = ticketCategorieList
                .stream()
                .collect(Collectors.toMap(TicketCategory::getProgramId, ticketCategory -> ticketCategory, (v1, v2) -> v2));
        
        Map<Long,String> areaMap = new HashMap<>();
        AreaDto areaDto = new AreaDto();
        areaDto.setIdList(iPage.getRecords().stream().map(Program::getAreaId).collect(Collectors.toList()));
        ApiResponse<List<AreaVo>> areaResponse = baseDataClient.selectByIdList(areaDto);
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
}
