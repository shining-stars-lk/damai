package com.damai.lock;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.damai.core.RepeatExecuteLimitConstants;
import com.damai.dto.ProgramOrderCreateDto;
import com.damai.dto.SeatDto;
import com.damai.locallock.LocalLockCache;
import com.damai.repeatexecutelimit.annotion.RepeatExecuteLimit;
import com.damai.service.ProgramOrderService;
import com.damai.servicelock.LockType;
import com.damai.servicelock.annotion.ServiceLock;
import com.damai.util.ServiceLockTool;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.damai.core.DistributedLockConstants.PROGRAM_ORDER_CREATE_V1;
import static com.damai.core.DistributedLockConstants.PROGRAM_ORDER_CREATE_V2;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 订单 中间层
 * @author: 阿宽不是程序员
 **/
@Slf4j
@Component
public class ProgramOrderLock {
    
    @Autowired
    private ProgramOrderService programOrderService;
    
    @Autowired
    private LocalLockCache localLockCache;
    
    @Autowired
    private ServiceLockTool serviceLockTool;
    
    /**
     * 订单创建，使用节目id作为锁
     * */
    @RepeatExecuteLimit(
            name = RepeatExecuteLimitConstants.CREATE_PROGRAM_ORDER,
            keys = {"#programOrderCreateDto.userId","#programOrderCreateDto.programId"})
    @ServiceLock(name = PROGRAM_ORDER_CREATE_V1,keys = {"#programOrderCreateDto.programId"})
    public String createV1(ProgramOrderCreateDto programOrderCreateDto) {
        return programOrderService.create(programOrderCreateDto);
    }
    
    /**
     * 订单优化版本v2，先用本地锁将没有获得锁的请求拦在外，获得本地锁后，再去获得分布式锁，这样可以减少对redis的压力。
     * 
     */
    @RepeatExecuteLimit(
            name = RepeatExecuteLimitConstants.CREATE_PROGRAM_ORDER,
            keys = {"#programOrderCreateDto.userId","#programOrderCreateDto.programId"})
    public String createV2(ProgramOrderCreateDto programOrderCreateDto) {
        List<SeatDto> seatDtoList = programOrderCreateDto.getSeatDtoList();
        List<Long> ticketCategoryIdList = new ArrayList<>();
        //根据入参座位或者票档id统计出要锁定的票档id
        if (CollectionUtil.isNotEmpty(seatDtoList)) {
            ticketCategoryIdList = seatDtoList.stream().map(SeatDto::getTicketCategoryId).distinct().collect(Collectors.toList());
        }else {
            ticketCategoryIdList.add(programOrderCreateDto.getTicketCategoryId());
        }
        //本地锁集合
        List<ReentrantLock> localLockList = new ArrayList<>(ticketCategoryIdList.size());
        //分布式锁集合
        List<ReentrantLock> localLockSuccessList = new ArrayList<>(ticketCategoryIdList.size());
        //加锁成功的本地锁集合
        List<RLock> serviceLockList = new ArrayList<>(ticketCategoryIdList.size());
        //加锁成功的分布式锁集合
        List<RLock> serviceLockSuccessList = new ArrayList<>(ticketCategoryIdList.size());
        //根据统计出的票档id获得本地锁和分布式锁集合
        for (Long ticketCategoryId : ticketCategoryIdList) {
            //锁的key为d_program_order_create_v2_lock-programId-ticketCategoryId
            String lockKey = StrUtil.join("-",PROGRAM_ORDER_CREATE_V2,programOrderCreateDto.getProgramId(),ticketCategoryId);
            ReentrantLock localLock = localLockCache.getLock(lockKey,true);
            RLock serviceLock = serviceLockTool.getLock(LockType.Fair, lockKey);
            localLockList.add(localLock);
            serviceLockList.add(serviceLock);
        }
        //循环本地锁进行加锁
        for (ReentrantLock reentrantLock : localLockList) {
            try {
                reentrantLock.lock();
            }catch (Throwable t) {
                break;
            }
            localLockSuccessList.add(reentrantLock);
        }
        //循环分布式锁进行加锁
        for (RLock rLock : serviceLockList) {
            try {
                rLock.lock();
            }catch (Throwable t) {
                break;
            }
            serviceLockSuccessList.add(rLock);
        }
        try {
            //进行订单创建
            return programOrderService.create(programOrderCreateDto);
        }finally {
            //先循环解锁分布式锁
            for (int i = serviceLockSuccessList.size() - 1; i >= 0; i--) {
                RLock rLock = serviceLockSuccessList.get(i);
                try {
                    rLock.unlock();
                }catch (Throwable t) {
                    log.error("service lock unlock error",t);
                }
            }
            //再循环解锁本地锁
            for (int i = localLockSuccessList.size() - 1; i >= 0; i--) {
                ReentrantLock reentrantLock = localLockSuccessList.get(i);
                try {
                    reentrantLock.unlock();
                }catch (Throwable t) {
                    log.error("local lock unlock error",t);
                }
            }
        }
    }
    
    /**
     * 订单创建，进行优化，一开始直接利用lua执行判断要购买的座位和票的数量是足够，不足够直接返回，足够的话进行相应扣除，
     * 这样既能将大量无用的抢购请求直接返回掉，又可以实现无锁化
     * */
    @RepeatExecuteLimit(
            name = RepeatExecuteLimitConstants.CREATE_PROGRAM_ORDER,
            keys = {"#programOrderCreateDto.userId","#programOrderCreateDto.programId"})
    public String createV3(ProgramOrderCreateDto programOrderCreateDto) {
        return programOrderService.createNew(programOrderCreateDto);
    }
}
