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
import com.damai.BusinessThreadPool;
import com.damai.client.BaseDataClient;
import com.damai.client.UserClient;
import com.damai.common.ApiResponse;
import com.damai.core.RedisKeyManage;
import com.damai.dto.AreaGetDto;
import com.damai.dto.AreaSelectDto;
import com.damai.dto.ProgramAddDto;
import com.damai.dto.ProgramGetDto;
import com.damai.dto.ProgramListDto;
import com.damai.dto.ProgramOperateDataDto;
import com.damai.dto.ProgramPageListDto;
import com.damai.dto.ProgramSearchDto;
import com.damai.dto.UserIdDto;
import com.damai.entity.Program;
import com.damai.entity.ProgramCategory;
import com.damai.entity.ProgramShowTime;
import com.damai.entity.ProgramJoinShowTime;
import com.damai.entity.Seat;
import com.damai.entity.TicketCategoryAggregate;
import com.damai.enums.BaseCode;
import com.damai.enums.BusinessStatus;
import com.damai.enums.SellStatus;
import com.damai.exception.DaMaiFrameException;
import com.damai.mapper.ProgramCategoryMapper;
import com.damai.mapper.ProgramMapper;
import com.damai.mapper.ProgramShowTimeMapper;
import com.damai.mapper.SeatMapper;
import com.damai.mapper.TicketCategoryMapper;
import com.damai.page.PageUtil;
import com.damai.page.PageVo;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyBuild;
import com.damai.service.constant.ProgramTimeType;
import com.damai.service.es.ProgramEs;
import com.damai.servicelock.LockType;
import com.damai.servicelock.annotion.ServiceLock;
import com.damai.threadlocal.BaseParameterHolder;
import com.damai.util.DateUtils;
import com.damai.util.StringUtil;
import com.damai.vo.AreaVo;
import com.damai.vo.ProgramListVo;
import com.damai.vo.ProgramVo;
import com.damai.vo.SeatVo;
import com.damai.vo.TicketCategoryVo;
import com.damai.vo.TicketUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import static com.damai.constant.Constant.CODE;
import static com.damai.constant.Constant.USER_ID;
import static com.damai.core.DistributedLockConstants.PROGRAM_LOCK;
import static com.damai.service.cache.ExpireTime.EXPIRE_TIME;
import static com.damai.util.DateUtils.FORMAT_DATE;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目 service
 * @author: 阿宽不是程序员
 **/
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
    private UserClient userClient;
    
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
    private ProgramEs programEs;
    
    public Long add(ProgramAddDto programAddDto){
        Program program = new Program();
        BeanUtil.copyProperties(programAddDto,program);
        program.setId(uidGenerator.getUid());
        programMapper.insert(program);
        return program.getId();
    }
    
    public PageVo<ProgramListVo> search(ProgramSearchDto programSearchDto) {
        setQueryTime(programSearchDto);
        return programEs.search(programSearchDto);
    }
    public Map<String,List<ProgramListVo>> selectHomeList(ProgramListDto programPageListDto) {
        
        Map<String, List<ProgramListVo>> programListVoMap = programEs.selectHomeList(programPageListDto);
        if (CollectionUtil.isNotEmpty(programListVoMap)) {
            return programListVoMap;
        }
        return dbSelectHomeList(programPageListDto);
    }
    
    private Map<String,List<ProgramListVo>> dbSelectHomeList(ProgramListDto programPageListDto){
        Map<String,List<ProgramListVo>> programListVoMap = new HashMap<>(256);
        Map<Long, String> programCategoryMap = selectProgramCategoryMap(programPageListDto.getParentProgramCategoryIds());
        
        List<Program> programList = programMapper.selectHomeList(programPageListDto);
        if (CollectionUtil.isEmpty(programList)) {
            return programListVoMap;
        }
        
        List<Long> programIdList = programList.stream().map(Program::getId).collect(Collectors.toList());
        LambdaQueryWrapper<ProgramShowTime> programShowTimeLambdaQueryWrapper = Wrappers.lambdaQuery(ProgramShowTime.class)
                .in(ProgramShowTime::getProgramId, programIdList);
        List<ProgramShowTime> programShowTimeList = programShowTimeMapper.selectList(programShowTimeLambdaQueryWrapper);
        Map<Long, List<ProgramShowTime>> programShowTimeMap = 
                programShowTimeList.stream().collect(Collectors.groupingBy(ProgramShowTime::getProgramId));
        
        Map<Long, TicketCategoryAggregate> ticketCategorieMap = selectTicketCategorieMap(programIdList);
        
        Map<Long, List<Program>> programMap = programList.stream()
                .collect(Collectors.groupingBy(Program::getParentProgramCategoryId));
        
        for (Entry<Long, List<Program>> programEntry : programMap.entrySet()) {
            Long key = programEntry.getKey();
            List<Program> value = programEntry.getValue();
            List<ProgramListVo> programListVoList = new ArrayList<>();
            for (Program program : value) {
                ProgramListVo programListVo = new ProgramListVo();
                BeanUtil.copyProperties(program,programListVo);
                
                programListVo.setShowTime(Optional.ofNullable(programShowTimeMap.get(program.getId()))
                        .filter(list -> !list.isEmpty())
                        .map(list -> list.get(0))
                        .map(ProgramShowTime::getShowTime)
                        .orElse(null));
                programListVo.setShowDayTime(Optional.ofNullable(programShowTimeMap.get(program.getId()))
                        .filter(list -> !list.isEmpty())
                        .map(list -> list.get(0))
                        .map(ProgramShowTime::getShowDayTime)
                        .orElse(null));
                programListVo.setShowWeekTime(Optional.ofNullable(programShowTimeMap.get(program.getId()))
                        .filter(list -> !list.isEmpty())
                        .map(list -> list.get(0))
                        .map(ProgramShowTime::getShowWeekTime)
                        .orElse(null));
                
                programListVo.setMaxPrice(Optional.ofNullable(ticketCategorieMap.get(program.getId()))
                        .map(TicketCategoryAggregate::getMaxPrice).orElse(null));
                programListVo.setMinPrice(Optional.ofNullable(ticketCategorieMap.get(program.getId()))
                        .map(TicketCategoryAggregate::getMinPrice).orElse(null));
                programListVoList.add(programListVo);
            }
            programListVoMap.put(programCategoryMap.get(key),programListVoList);
        }
        return programListVoMap;
    }
    
    public void setQueryTime(ProgramPageListDto programPageListDto){
        switch (programPageListDto.getTimeType()) {
            case ProgramTimeType.TODAY:
                programPageListDto.setStartDateTime(DateUtils.now(FORMAT_DATE));
                programPageListDto.setEndDateTime(DateUtils.now(FORMAT_DATE));
                break;
            case ProgramTimeType.TOMORROW:
                programPageListDto.setStartDateTime(DateUtils.now(FORMAT_DATE));
                programPageListDto.setEndDateTime(DateUtils.addDay(DateUtils.now(FORMAT_DATE),1));
                break;
            case ProgramTimeType.WEEK:
                programPageListDto.setStartDateTime(DateUtils.now(FORMAT_DATE));
                programPageListDto.setEndDateTime(DateUtils.addWeek(DateUtils.now(FORMAT_DATE),1));
                break;
            case ProgramTimeType.MONTH:
                programPageListDto.setStartDateTime(DateUtils.now(FORMAT_DATE));
                programPageListDto.setEndDateTime(DateUtils.addMonth(DateUtils.now(FORMAT_DATE),1));
                break;
            case ProgramTimeType.CALENDAR:
                if (Objects.isNull(programPageListDto.getStartDateTime())) {
                    throw new DaMaiFrameException(BaseCode.START_DATE_TIME_NOT_EXIST);
                }
                if (Objects.isNull(programPageListDto.getEndDateTime())) {
                    throw new DaMaiFrameException(BaseCode.END_DATE_TIME_NOT_EXIST);
                }
                break;
            default:
                programPageListDto.setStartDateTime(null);
                programPageListDto.setEndDateTime(null);
                break;
        }
    }
    public PageVo<ProgramListVo> selectPage(ProgramPageListDto programPageListDto) {
        setQueryTime(programPageListDto);
        PageVo<ProgramListVo> pageVo = programEs.selectPage(programPageListDto);
        if (CollectionUtil.isNotEmpty(pageVo.getList())) {
            return pageVo;
        }
        return dbSelectPage(programPageListDto);
    }
    public PageVo<ProgramListVo> dbSelectPage(ProgramPageListDto programPageListDto) {
        IPage<ProgramJoinShowTime> iPage = 
                programMapper.selectPage(PageUtil.getPageParams(programPageListDto), programPageListDto);
        if (CollectionUtil.isEmpty(iPage.getRecords())) {
            return new PageVo<>(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), new ArrayList<>());
        }
        Set<Long> programCategoryIdList = 
                iPage.getRecords().stream().map(Program::getProgramCategoryId).collect(Collectors.toSet());
        Map<Long, String> programCategoryMap = selectProgramCategoryMap(programCategoryIdList);
        
        List<Long> programIdList = iPage.getRecords().stream().map(Program::getId).collect(Collectors.toList());
        Map<Long, TicketCategoryAggregate> ticketCategorieMap = selectTicketCategorieMap(programIdList);
        
        Map<Long,String> tempAreaMap = new HashMap<>(64);
        AreaSelectDto areaSelectDto = new AreaSelectDto();
        areaSelectDto.setIdList(iPage.getRecords().stream().map(Program::getAreaId).distinct().collect(Collectors.toList()));
        ApiResponse<List<AreaVo>> areaResponse = baseDataClient.selectByIdList(areaSelectDto);
        if (Objects.equals(areaResponse.getCode(), ApiResponse.ok().getCode())) {
            if (CollectionUtil.isNotEmpty(areaResponse.getData())) {
                tempAreaMap = areaResponse.getData().stream()
                        .collect(Collectors.toMap(AreaVo::getId,AreaVo::getName,(v1,v2) -> v2));
            }
        }else {
            log.error("base-data selectByIdList rpc error areaResponse:{}", JSON.toJSONString(areaResponse));
        }
        Map<Long,String> areaMap = tempAreaMap;
        
        return PageUtil.convertPage(iPage, programJoinShowTime -> {
            ProgramListVo programListVo = new ProgramListVo();
            BeanUtil.copyProperties(programJoinShowTime, programListVo);
            
            programListVo.setAreaName(areaMap.get(programJoinShowTime.getAreaId()));
            programListVo.setProgramCategoryName(programCategoryMap.get(programJoinShowTime.getProgramCategoryId()));
            programListVo.setMinPrice(Optional.ofNullable(ticketCategorieMap.get(programJoinShowTime.getId()))
                    .map(TicketCategoryAggregate::getMinPrice).orElse(null));
            programListVo.setMaxPrice(Optional.ofNullable(ticketCategorieMap.get(programJoinShowTime.getId()))
                    .map(TicketCategoryAggregate::getMaxPrice).orElse(null));
            return programListVo;
        });
    }
    
    
    public ProgramVo getDetail(ProgramGetDto programGetDto) {
        ProgramVo redisProgramVo = programService.getById(programGetDto.getId());
        
        preloadTicketUserList(redisProgramVo.getHighHeat());
        
        setProgramCategoryData(redisProgramVo);
        
        ProgramShowTime redisProgramShowTime = programShowTimeService.selectProgramShowTimeByProgramId(redisProgramVo.getId());
        redisProgramVo.setShowTime(redisProgramShowTime.getShowTime());
        redisProgramVo.setShowDayTime(redisProgramShowTime.getShowDayTime());
        redisProgramVo.setShowWeekTime(redisProgramShowTime.getShowWeekTime());
        
        List<SeatVo> redisSeatVoList = seatService.selectSeatByProgramId(redisProgramVo.getId());
        if (Objects.equals(redisProgramVo.getPermitChooseSeat(), BusinessStatus.YES.getCode())) {
            redisProgramVo.setSeatVoList(redisSeatVoList);
        }
        
        List<TicketCategoryVo> ticketCategoryVoList = ticketCategoryService.selectTicketCategoryListByProgramId(redisProgramVo.getId());
        redisProgramVo.setTicketCategoryVoList(ticketCategoryVoList);
        
        ticketCategoryService.setRedisRemainNumber(redisProgramVo.getId());
        
        return redisProgramVo;
    }
    
    @ServiceLock(lockType= LockType.Read,name = PROGRAM_LOCK,keys = {"#programId"})
    public ProgramVo getById(Long programId) {
        return redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM,programId)
                ,ProgramVo.class,
                () -> createProgramVo(programId)
                ,EXPIRE_TIME, 
                TimeUnit.DAYS);
    }
    
    public Map<Long, String> selectProgramCategoryMap(Collection<Long> programCategoryIdList){
        LambdaQueryWrapper<ProgramCategory> pcLambdaQueryWrapper = Wrappers.lambdaQuery(ProgramCategory.class)
                .in(ProgramCategory::getId, programCategoryIdList);
        List<ProgramCategory> programCategoryList = programCategoryMapper.selectList(pcLambdaQueryWrapper);
        return programCategoryList
                .stream()
                .collect(Collectors.toMap(ProgramCategory::getId, ProgramCategory::getName, (v1, v2) -> v2));
    }
    
    public Map<Long, TicketCategoryAggregate> selectTicketCategorieMap(List<Long> programIdList){
        List<TicketCategoryAggregate> ticketCategorieList = ticketCategoryMapper.selectAggregateList(programIdList);
        return ticketCategorieList
                .stream()
                .collect(Collectors.toMap(TicketCategoryAggregate::getProgramId, ticketCategory -> ticketCategory, (v1, v2) -> v2));
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void operateProgramData(ProgramOperateDataDto programOperateDataDto){
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
    
    private ProgramVo createProgramVo(long programId){
        ProgramVo programVo = new ProgramVo();
        Program program = 
                Optional.ofNullable(programMapper.selectById(programId))
                        .orElseThrow(() -> new DaMaiFrameException(BaseCode.PROGRAM_NOT_EXIST));
        BeanUtil.copyProperties(program,programVo);
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
    }
    
    public List<Long> getAllProgramIdList(){
        LambdaQueryWrapper<Program> programLambdaQueryWrapper =
                Wrappers.lambdaQuery(Program.class).eq(Program::getProgramStatus, BusinessStatus.YES.getCode())
                        .select(Program::getId);
        List<Program> programs = programMapper.selectList(programLambdaQueryWrapper);
        return programs.stream().map(Program::getId).collect(Collectors.toList());
    }
    
    public ProgramVo getDetailFromDb(Long programId) {
        ProgramVo programVo = createProgramVo(programId);
        
        setProgramCategoryData(programVo);
        
        LambdaQueryWrapper<ProgramShowTime> programShowTimeLambdaQueryWrapper =
                Wrappers.lambdaQuery(ProgramShowTime.class).eq(ProgramShowTime::getProgramId, programId);
        ProgramShowTime programShowTime = Optional.ofNullable(programShowTimeMapper.selectOne(programShowTimeLambdaQueryWrapper))
                .orElseThrow(() -> new DaMaiFrameException(BaseCode.PROGRAM_SHOW_TIME_NOT_EXIST));
        
        programVo.setShowTime(programShowTime.getShowTime());
        programVo.setShowDayTime(programShowTime.getShowDayTime());
        programVo.setShowWeekTime(programShowTime.getShowWeekTime());
        
        return programVo;
    }
    
    private void preloadTicketUserList(Integer highHeat){
        if (Objects.equals(highHeat, BusinessStatus.NO.getCode())) {
            return;
        }
        String userId = BaseParameterHolder.getParameter(USER_ID);
        String code = BaseParameterHolder.getParameter(CODE);
        if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(code)) {
            return;
        }
        BusinessThreadPool.execute(() -> {
            try {
                Boolean userLogin = redisCache.hasKey(RedisKeyBuild.createRedisKey(RedisKeyManage.USER_LOGIN, code, userId));
                if (!userLogin) {
                    return;
                }
                if (redisCache.hasKey(RedisKeyBuild.createRedisKey(RedisKeyManage.TICKET_USER_LIST,userId))) {
                    return;
                }
                UserIdDto userIdDto = new UserIdDto();
                userIdDto.setId(Long.parseLong(userId));
                ApiResponse<List<TicketUserVo>> apiResponse = userClient.select(userIdDto);
                if (Objects.equals(apiResponse.getCode(), BaseCode.SUCCESS.getCode())) {
                    Optional.ofNullable(apiResponse.getData()).filter(CollectionUtil::isNotEmpty)
                            .ifPresent(ticketUserVoList -> redisCache.set(RedisKeyBuild.createRedisKey(
                                    RedisKeyManage.TICKET_USER_LIST,userId),ticketUserVoList));
                }else {
                    log.warn("userClient.select 调用失败 apiResponse : {}",JSON.toJSONString(apiResponse));
                }
            }catch (Exception e) {
                log.error("预热加载投票人列表失败",e);
            }
        });
    }
    
    public void setProgramCategoryData(ProgramVo programVo){
        ProgramCategory programCategory = redisCache.getForHash(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_CATEGORY_HASH)
                ,String.valueOf(programVo.getProgramCategoryId()),ProgramCategory.class);
        if (Objects.nonNull(programCategory)) {
            programVo.setProgramCategoryName(programCategory.getName());
        }
        ProgramCategory parentProgramCategory = redisCache.getForHash(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_CATEGORY_HASH)
                ,String.valueOf(programVo.getParentProgramCategoryId()),ProgramCategory.class);
        if (Objects.nonNull(parentProgramCategory)) {
            programVo.setParentProgramCategoryName(parentProgramCategory.getName());
        }
    }
}
