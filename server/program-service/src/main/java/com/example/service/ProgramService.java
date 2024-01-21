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
import com.example.core.RedisKeyEnum;
import com.example.dto.AreaGetDto;
import com.example.dto.AreaSelectDto;
import com.example.dto.ProgramGetDto;
import com.example.dto.ProgramListDto;
import com.example.dto.ProgramPageListDto;
import com.example.entity.Program;
import com.example.entity.ProgramCategory;
import com.example.entity.ProgramShowTime;
import com.example.entity.ProgramV2;
import com.example.entity.TicketCategoryAggregate;
import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;
import com.example.mapper.ProgramCategoryMapper;
import com.example.mapper.ProgramMapper;
import com.example.mapper.ProgramShowTimeMapper;
import com.example.mapper.SeatMapper;
import com.example.mapper.TicketCategoryMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.redisson.LockType;
import com.example.servicelock.annotion.ServiceLock;
import com.example.util.PageUtil;
import com.example.vo.AreaVo;
import com.example.vo.ProgramListVo;
import com.example.vo.ProgramVo;
import com.example.vo.SeatVo;
import com.example.vo.TicketCategoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.core.DistributedLockConstants.PROGRAM_LOCK;
import static com.example.service.cache.ExpireTime.EXPIRE_TIME;

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
    private SeatMapper seatMapper;
    
    @Autowired
    private BaseDataClient baseDataClient;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ProgramService programService;
    
    @Autowired
    private ProgramShowTimeService programShowTimeService;
    
    @Autowired
    private SeatService seatService;
    
    @Autowired
    private TicketCategoryService ticketCategoryService;
    
    public Map<String,List<ProgramListVo>> selectHomeList(ProgramListDto programPageListDto) {
        Map<String,List<ProgramListVo>> programListVoMap = new HashMap<>();
        
        //根据区域id和父节目类型id查询节目列表
        LambdaQueryWrapper<Program> programLambdaQueryWrapper = Wrappers.lambdaQuery(Program.class)
                .eq(Program::getAreaId,programPageListDto.getAreaId())
                .in(Program::getParentProgramCategoryId, programPageListDto.getParentProgramCategoryIds());
        List<Program> programList = programMapper.selectList(programLambdaQueryWrapper);
        //节目id集合
        List<Long> programIdList = programList.stream().map(Program::getId).collect(Collectors.toList());
        //根据节目id集合查询节目演出时间集合
        LambdaQueryWrapper<ProgramShowTime> programShowTimeLambdaQueryWrapper = Wrappers.lambdaQuery(ProgramShowTime.class)
                .in(ProgramShowTime::getProgramId, programIdList);
        List<ProgramShowTime> programShowTimeList = programShowTimeMapper.selectList(programShowTimeLambdaQueryWrapper);
        //将节目演出集合根据节目id进行分组成map，key：节目id，value：节目演出时间集合
        Map<Long, List<ProgramShowTime>> ProgramShowTimeMap = programShowTimeList.stream().collect(Collectors.groupingBy(ProgramShowTime::getProgramId));
        
        //根据获得的节目列表中的父节目类型id来查询节目类型map，key：节目类型id，value：节目类型名
        Map<Long, String> programCategoryMap = selectProgramCategoryMap(programList.stream().map(Program::getParentProgramCategoryId).collect(Collectors.toList()));
        
        //根据节目id统计出票档的最低价和最高价的集合map, key：节目id，value：票档
        Map<Long, TicketCategoryAggregate> ticketCategorieMap = selectTicketCategorieMap(programIdList);
        
        
        //将节目结合按照父节目类型id进行分组成map，key：父节目类型id，value：节目集合
        Map<Long, List<Program>> programMap = programList.stream().collect(Collectors.groupingBy(Program::getParentProgramCategoryId));
        //循环此map
        for (Entry<Long, List<Program>> programEntry : programMap.entrySet()) {
            //父节目类型id
            Long key = programEntry.getKey();
            //节目集合
            List<Program> value = programEntry.getValue();
            List<ProgramListVo> programListVoList = new ArrayList<>();
            //循环节目集合
            for (Program program : value) {
                ProgramListVo programListVo = new ProgramListVo();
                BeanUtil.copyProperties(program,programListVo);
                //演出时间
                programListVo.setShowTime(Optional.ofNullable(ProgramShowTimeMap.get(program.getId()))
                        .filter(list -> !list.isEmpty())
                        .map(list -> list.get(0))
                        .map(ProgramShowTime::getShowTime)
                        .orElse(null));
                //演出时间(精确到天)
                programListVo.setShowDayTime(Optional.ofNullable(ProgramShowTimeMap.get(program.getId()))
                        .filter(list -> !list.isEmpty())
                        .map(list -> list.get(0))
                        .map(ProgramShowTime::getShowDayTime)
                        .orElse(null));
                //演出时间所在的星期
                programListVo.setShowWeekTime(Optional.ofNullable(ProgramShowTimeMap.get(program.getId()))
                        .filter(list -> !list.isEmpty())
                        .map(list -> list.get(0))
                        .map(ProgramShowTime::getShowWeekTime)
                        .orElse(null));
                //节目最高价
                programListVo.setMaxPrice(Optional.ofNullable(ticketCategorieMap.get(program.getId())).map(TicketCategoryAggregate::getMaxPrice).orElse(null));
                //节目最低价
                programListVo.setMinPrice(Optional.ofNullable(ticketCategorieMap.get(program.getId())).map(TicketCategoryAggregate::getMinPrice).orElse(null));
                programListVoList.add(programListVo);
            }
            //key：节目类型名 value：节目列表
            programListVoMap.put(programCategoryMap.get(key),programListVoList);
        }
        return programListVoMap;
    }
    public IPage<ProgramListVo> selectPage(ProgramPageListDto programPageListDto) {
        //需要program和program_show_time连表查询
        IPage<ProgramV2> iPage = programMapper.selectPage(PageUtil.getPageParams(programPageListDto), programPageListDto);
        
        //根据节目列表获得节目类型id列表
        Set<Long> programCategoryIdList = iPage.getRecords().stream().map(Program::getProgramCategoryId).collect(Collectors.toSet());
        //根据id来查询节目类型列表map，key：节目类型id，value：节目类型名
        Map<Long, String> programCategoryMap = selectProgramCategoryMap(programCategoryIdList);
        
        //节目id集合
        List<Long> programIdList = iPage.getRecords().stream().map(Program::getId).collect(Collectors.toList());
        //根据节目id统计出票档的最低价和最高价的集合map, key：节目id，value：票档
        Map<Long, TicketCategoryAggregate> ticketCategorieMap = selectTicketCategorieMap(programIdList);
        //查询区域
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
            //区域名字
            programListVo.setAreaName(areaMap.get(programV2.getAreaId()));
            //节目名字
            programListVo.setProgramCategoryName(programCategoryMap.get(programV2.getProgramCategoryId()));
            //最低价
            programListVo.setMinPrice(Optional.ofNullable(ticketCategorieMap.get(programV2.getId())).map(TicketCategoryAggregate::getMinPrice).orElse(null));
            //最高价
            programListVo.setMaxPrice(Optional.ofNullable(ticketCategorieMap.get(programV2.getId())).map(TicketCategoryAggregate::getMaxPrice).orElse(null));
            programListVoList.add(programListVo);
        }
        return PageUtil.convertPage(iPage,programListVoList);
    }
    
    public ProgramVo getDetail(ProgramGetDto programGetDto) {
        ProgramVo redisProgramVo = programService.getById(programGetDto.getId());
        
        //查询节目类型
        ProgramCategory programCategory = redisCache.getForHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_CATEGORY_HASH)
                ,String.valueOf(redisProgramVo.getProgramCategoryId()),ProgramCategory.class);
        if (Objects.nonNull(programCategory)) {
            redisProgramVo.setProgramCategoryName(programCategory.getName());
        }
        
        //查询节目演出时间
        ProgramShowTime redisProgramShowTime = programShowTimeService.selectProgramShowTimeByProgramId(redisProgramVo.getId());
        redisProgramVo.setShowTime(redisProgramShowTime.getShowTime());
        redisProgramVo.setShowDayTime(redisProgramShowTime.getShowDayTime());
        redisProgramVo.setShowWeekTime(redisProgramShowTime.getShowWeekTime());
        
        
        //查询座位
        List<SeatVo> redisSeatVoList = seatService.selectSeatByProgramId(redisProgramVo.getId());
        redisProgramVo.setSeatVoList(redisSeatVoList);
        
        //查询节目票档
        List<TicketCategoryVo> ticketCategoryVoList = ticketCategoryService.selectTicketCategoryListByProgramId(redisProgramVo.getId());
        redisProgramVo.setTicketCategoryVoList(ticketCategoryVoList);
        
        //余票数量
        ticketCategoryService.setRedisRemainNumber(redisProgramVo.getId());
        
        return redisProgramVo;
    }
    
    @ServiceLock(lockType= LockType.Read,name = PROGRAM_LOCK,keys = {"#programId"})
    public ProgramVo getById(Long programId) {
        return redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM,programId),ProgramVo.class,() -> {
            ProgramVo programVo = new ProgramVo();
            //根据id查询到节目
            Program program = Optional.ofNullable(programMapper.selectById(programId)).orElseThrow(() -> new CookFrameException(BaseCode.PROGRAM_NOT_EXIST));
            BeanUtil.copyProperties(program,programVo);
            
            //查询区域
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
            return programVo;
        },EXPIRE_TIME, TimeUnit.DAYS);
    }
    /**
     * 根据节目类型id集合查询节目类型名字
     * @param programCategoryIdList 节目类型id集合
     * @return Map<Long, String> key：节目类型id value：节目类型名字
     * */
    public Map<Long, String> selectProgramCategoryMap(Collection<Long> programCategoryIdList){
        LambdaQueryWrapper<ProgramCategory> pcLambdaQueryWrapper = Wrappers.lambdaQuery(ProgramCategory.class)
                .in(ProgramCategory::getId, programCategoryIdList);
        List<ProgramCategory> programCategoryList = programCategoryMapper.selectList(pcLambdaQueryWrapper);
        return programCategoryList
                .stream()
                .collect(Collectors.toMap(ProgramCategory::getId, ProgramCategory::getName, (v1, v2) -> v2));
    }
    
    public Map<Long, TicketCategoryAggregate> selectTicketCategorieMap(List<Long> programIdList){
        //根据节目id统计出票档的最低价和最高价的集合
        List<TicketCategoryAggregate> ticketCategorieList = ticketCategoryMapper.selectAggregateList(programIdList);
        //将集合转换为map，key：节目id，value：票档
        Map<Long, TicketCategoryAggregate> ticketCategorieMap = ticketCategorieList
                .stream()
                .collect(Collectors.toMap(TicketCategoryAggregate::getProgramId, ticketCategory -> ticketCategory, (v1, v2) -> v2));
        return ticketCategorieMap;
    }
    
}
