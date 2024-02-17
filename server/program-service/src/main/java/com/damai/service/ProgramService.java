package com.damai.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.damai.client.BaseDataClient;
import com.damai.common.ApiResponse;
import com.damai.core.RedisKeyEnum;
import com.damai.dto.AreaGetDto;
import com.damai.dto.AreaSelectDto;
import com.damai.dto.EsDataQueryDto;
import com.damai.dto.ProgramAddDto;
import com.damai.dto.ProgramGetDto;
import com.damai.dto.ProgramListDto;
import com.damai.dto.ProgramOperateDataDto;
import com.damai.dto.ProgramPageListDto;
import com.damai.dto.ProgramSearchDto;
import com.damai.entity.Program;
import com.damai.entity.ProgramCategory;
import com.damai.entity.ProgramShowTime;
import com.damai.entity.ProgramV2;
import com.damai.entity.Seat;
import com.damai.entity.TicketCategoryAggregate;
import com.damai.enums.BaseCode;
import com.damai.enums.BusinessStatus;
import com.damai.enums.SellStatus;
import com.damai.enums.TimeType;
import com.damai.exception.DaMaiFrameException;
import com.damai.mapper.ProgramCategoryMapper;
import com.damai.mapper.ProgramMapper;
import com.damai.mapper.ProgramShowTimeMapper;
import com.damai.mapper.SeatMapper;
import com.damai.mapper.TicketCategoryMapper;
import com.damai.page.PageUtil;
import com.damai.page.PageVo;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyWrap;
import com.damai.service.init.ProgramDocumentParamName;
import com.damai.service.pagestrategy.SelectPageWrapper;
import com.damai.servicelock.LockType;
import com.damai.servicelock.annotion.ServiceLock;
import com.damai.util.BusinessEsHandle;
import com.damai.util.DateUtils;
import com.damai.vo.AreaVo;
import com.damai.vo.ProgramListVo;
import com.damai.vo.ProgramVo;
import com.damai.vo.SeatVo;
import com.damai.vo.TicketCategoryVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
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

import static com.damai.core.DistributedLockConstants.PROGRAM_LOCK;
import static com.damai.service.cache.ExpireTime.EXPIRE_TIME;

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
    private UidGenerator uidGenerator;
    
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
    
    @Autowired
    private BusinessEsHandle businessEsHandle;
    
    @Autowired
    private SelectPageWrapper selectPageWrapper;
    
    public Long add(ProgramAddDto programAddDto){
        Program program = new Program();
        BeanUtil.copyProperties(programAddDto,program);
        program.setId(uidGenerator.getUID());
        programMapper.insert(program);
        return program.getId();
    }
    
    public PageVo<ProgramListVo> search(ProgramSearchDto programSearchDto) {
        PageVo<ProgramListVo> pageVo = new PageVo<>();
        try {
            List<EsDataQueryDto> esDataQueryDtoList = new ArrayList<>();
            EsDataQueryDto titleQueryDto = new EsDataQueryDto();
            titleQueryDto.setParamName(ProgramDocumentParamName.TITLE);
            titleQueryDto.setParamValue(programSearchDto.getTitle());
            titleQueryDto.setAnalyse(true);
            esDataQueryDtoList.add(titleQueryDto);
            PageInfo<ProgramListVo> programListVoPageInfo = businessEsHandle.queryPage(ProgramDocumentParamName.INDEX_NAME, 
                    ProgramDocumentParamName.INDEX_TYPE, esDataQueryDtoList, programSearchDto.getPageNumber(), 
                    programSearchDto.getPageSize(), ProgramListVo.class);
            pageVo = PageUtil.convertPage(programListVoPageInfo,programListVo -> programListVo);
        }catch (Exception e) {
            log.error("search error",e);
        }
        return pageVo;
    }
    public Map<String,List<ProgramListVo>> selectHomeList(ProgramListDto programPageListDto) {
        Map<String,List<ProgramListVo>> programListVoMap = new HashMap<>();
        
        //根据区域id和父节目类型id查询节目列表
        LambdaQueryWrapper<Program> programLambdaQueryWrapper = Wrappers.lambdaQuery(Program.class)
                .eq(Program::getProgramStatus, BusinessStatus.YES.getCode())
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
    
    public PageVo<ProgramListVo> selectPage(ProgramPageListDto programPageListDto) {
        return selectPageWrapper.selectPage(programPageListDto);
    }
    public PageVo<ProgramListVo> doSelectPage(ProgramPageListDto programPageListDto) {
        //需要program和program_show_time连表查询
        if (Objects.nonNull(programPageListDto.getTimeType())) {
            if (programPageListDto.getTimeType().equals(TimeType.WEEK.getCode())) {
                programPageListDto.setTime(DateUtils.addWeek(DateUtils.now(),1));
            }else if (programPageListDto.getTimeType().equals(TimeType.MONTH.getCode())) {
                programPageListDto.setTime(DateUtils.addMonth(DateUtils.now(),1));
            }
        }
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
        Map<Long,String> tempAreaMap = new HashMap<>();
        AreaSelectDto areaSelectDto = new AreaSelectDto();
        areaSelectDto.setIdList(iPage.getRecords().stream().map(Program::getAreaId).distinct().collect(Collectors.toList()));
        ApiResponse<List<AreaVo>> areaResponse = baseDataClient.selectByIdList(areaSelectDto);
        if (Objects.equals(areaResponse.getCode(), ApiResponse.ok().getCode())) {
            if (CollectionUtil.isNotEmpty(areaResponse.getData())) {
                tempAreaMap = areaResponse.getData().stream().collect(Collectors.toMap(AreaVo::getId,AreaVo::getName,(v1,v2) -> v2));
            }
        }else {
            log.error("base-data selectByIdList rpc error areaResponse:{}", JSON.toJSONString(areaResponse));
        }
        
        Map<Long,String> areaMap = tempAreaMap;
        return PageUtil.convertPage(iPage,programV2 -> {
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
            return programListVo;
        });
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
            Program program = Optional.ofNullable(programMapper.selectById(programId)).orElseThrow(() -> new DaMaiFrameException(BaseCode.PROGRAM_NOT_EXIST));
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
        return ticketCategorieList
                .stream()
                .collect(Collectors.toMap(TicketCategoryAggregate::getProgramId, ticketCategory -> ticketCategory, (v1, v2) -> v2));
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void OperateProgramData(ProgramOperateDataDto programOperateDataDto){
        Map<Long, Long> ticketCategoryCountMap = programOperateDataDto.getTicketCategoryCountMap();
        //从库中查询座位集合
        List<Long> seatIdList = programOperateDataDto.getSeatIdList();
        LambdaQueryWrapper<Seat> seatLambdaQueryWrapper = 
                Wrappers.lambdaQuery(Seat.class).in(Seat::getId, seatIdList);
        List<Seat> seatList = seatMapper.selectList(seatLambdaQueryWrapper);
        //如果库中的座位集合为空或者库中的座位集合数量和传入的座位数量不相同则抛出异常
        if (CollectionUtil.isEmpty(seatList) || seatList.size() != seatIdList.size()) {
            throw new DaMaiFrameException(BaseCode.SEAT_NOT_EXIST);
        }
        for (Seat seat : seatList) {
            //如果库中的座位有一个已经是已售卖的状态，则抛出异常
            if (Objects.equals(seat.getSellStatus(), SellStatus.SOLD.getCode())) {
                throw new DaMaiFrameException(BaseCode.SEAT_SOLD);
            }
        }
        //将库中的座位集合批量更新为售卖状态
        LambdaUpdateWrapper<Seat> seatLambdaUpdateWrapper = 
                Wrappers.lambdaUpdate(Seat.class).in(Seat::getId, seatIdList);
        Seat updateSeat = new Seat();
        updateSeat.setSellStatus(SellStatus.SOLD.getCode());
        seatMapper.update(updateSeat,seatLambdaUpdateWrapper);
        
        //将库中的对应票档进行更新库存
        int updateRemainNumberCount = 
                ticketCategoryMapper.batchUpdateRemainNumber(ticketCategoryCountMap);
        if (updateRemainNumberCount != ticketCategoryCountMap.size()) {
            throw new DaMaiFrameException(BaseCode.UPDATE_TICKET_CATEGORY_COUNT_NOT_CORRECT);
        }
    }
    
    public List<Long> getAllProgramIdList(){
        LambdaQueryWrapper<Program> programLambdaQueryWrapper =
                Wrappers.lambdaQuery(Program.class).eq(Program::getProgramStatus, BusinessStatus.YES.getCode())
                        .select(Program::getId);
        List<Program> programs = programMapper.selectList(programLambdaQueryWrapper);
        return programs.stream().map(Program::getId).collect(Collectors.toList());
    }
    
    public ProgramVo getDetailFromDb(Long programId) {
        ProgramVo programVo = new ProgramVo();
        //根据id查询到节目
        Program program = Optional.ofNullable(programMapper.selectById(programId)).orElseThrow(() -> new DaMaiFrameException(BaseCode.PROGRAM_NOT_EXIST));
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
        
        //查询节目类型
        ProgramCategory programCategory = redisCache.getForHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_CATEGORY_HASH)
                ,String.valueOf(programVo.getProgramCategoryId()),ProgramCategory.class);
        if (Objects.nonNull(programCategory)) {
            programVo.setProgramCategoryName(programCategory.getName());
        }
        
        //查询节目演出时间
        LambdaQueryWrapper<ProgramShowTime> programShowTimeLambdaQueryWrapper =
                Wrappers.lambdaQuery(ProgramShowTime.class).eq(ProgramShowTime::getProgramId, programId);
        ProgramShowTime programShowTime = Optional.ofNullable(programShowTimeMapper.selectOne(programShowTimeLambdaQueryWrapper))
                .orElseThrow(() -> new DaMaiFrameException(BaseCode.PROGRAM_SHOW_TIME_NOT_EXIST));
        
        
        programVo.setShowTime(programShowTime.getShowTime());
        programVo.setShowDayTime(programShowTime.getShowDayTime());
        programVo.setShowWeekTime(programShowTime.getShowWeekTime());
        
        return programVo;
    }
    
    public void indexAdd(@Valid @RequestBody ProgramGetDto programGetDto) {
        
    }
    
    public ProgramVo dataAdd(@Valid @RequestBody ProgramGetDto programGetDto) {
        return null;
    }
}
