package com.example.lock;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.dto.ProgramOrderCreateDto;
import com.example.dto.SeatDto;
import com.example.redisson.LockType;
import com.example.service.ProgramOrderService;
import com.example.service.locallock.LocalLockCache;
import com.example.servicelock.annotion.ServiceLock;
import com.example.util.ServiceLockTool;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.example.core.DistributedLockConstants.PROGRAM_ORDER_CREATE_V1;
import static com.example.core.DistributedLockConstants.PROGRAM_ORDER_CREATE_V2;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-02-05
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
    
    @ServiceLock(name = PROGRAM_ORDER_CREATE_V1,keys = {"#programOrderCreateDto.programId"})
    public String createV1(ProgramOrderCreateDto programOrderCreateDto) {
        return programOrderService.create(programOrderCreateDto);
    }
    
    public String createV2(ProgramOrderCreateDto programOrderCreateDto) {
        List<SeatDto> seatDtoList = programOrderCreateDto.getSeatDtoList();
        List<Long> ticketCategoryIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(seatDtoList)) {
            ticketCategoryIdList = seatDtoList.stream().map(SeatDto::getTicketCategoryId).distinct().collect(Collectors.toList());
        }else {
            ticketCategoryIdList.add(programOrderCreateDto.getTicketCategoryId());
        }
        List<ReentrantLock> localLockList = new ArrayList<>(ticketCategoryIdList.size());
        List<ReentrantLock> localLockSuccessList = new ArrayList<>(ticketCategoryIdList.size());
        List<RLock> serviceLockList = new ArrayList<>(ticketCategoryIdList.size());
        List<RLock> serviceLockSuccessList = new ArrayList<>(ticketCategoryIdList.size());
        for (Long ticketCategoryId : ticketCategoryIdList) {
            String lockKey = StrUtil.join("-",PROGRAM_ORDER_CREATE_V2,programOrderCreateDto.getProgramId(),ticketCategoryId);
            ReentrantLock localLock = localLockCache.getLock(lockKey,true);
            RLock serviceLock = serviceLockTool.getLock(LockType.Fair, lockKey);
            localLockList.add(localLock);
            serviceLockList.add(serviceLock);
        }
        for (ReentrantLock reentrantLock : localLockList) {
            try {
                reentrantLock.lock();
            }catch (Throwable t) {
                break;
            }
            localLockSuccessList.add(reentrantLock);
        }
        for (RLock rLock : serviceLockList) {
            try {
                rLock.lock();
            }catch (Throwable t) {
                break;
            }
            serviceLockSuccessList.add(rLock);
        }
        try {
            return programOrderService.create(programOrderCreateDto);
        }finally {
            for (int i = serviceLockSuccessList.size() - 1; i >= 0; i--) {
                RLock rLock = serviceLockSuccessList.get(i);
                try {
                    rLock.unlock();
                }catch (Throwable t) {
                    log.error("service lock unlock error",t);
                }
            }
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
    
    public String createV3(ProgramOrderCreateDto programOrderCreateDto) {
        return programOrderService.createNew(programOrderCreateDto);
    }
}
