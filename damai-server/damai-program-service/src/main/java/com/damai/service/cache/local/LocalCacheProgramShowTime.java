package com.damai.service.cache.local;

import com.damai.entity.ProgramShowTime;
import com.damai.util.DateUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目演出时间本地缓存
 * @author: 阿星不是程序员
 **/
@Component
public class LocalCacheProgramShowTime {
    
    /**
     * 本地缓存
     * */
    private Cache<String, ProgramShowTime> localCache;
    
    
    /**
     * 本地缓存的容量
     * */
    @Value("${maximumSize:10000}")
    private Long maximumSize;
    
    @PostConstruct
    public void localLockCacheInit(){
        localCache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfter(new Expiry<String, ProgramShowTime>() {
                    @Override
                    public long expireAfterCreate(@NonNull final String key, @NonNull final ProgramShowTime value, 
                                                  final long currentTime) {
                        return TimeUnit.SECONDS.toNanos(DateUtils.countBetweenSecond(DateUtils.now(),value.getShowTime()));
                    }
                    
                    @Override
                    public long expireAfterUpdate(@NonNull final String key, @NonNull final ProgramShowTime value, 
                                                  final long currentTime, @NonNegative final long currentDuration) {
                        return currentDuration;
                    }
                    
                    @Override
                    public long expireAfterRead(@NonNull final String key, @NonNull final ProgramShowTime value, 
                                                final long currentTime, @NonNegative final long currentDuration) {
                        return currentDuration;
                    }
                })
                .build();
    }
    
    /**
     * Caffeine的get是线程安全的
     * */
    public ProgramShowTime getCache(String id, Function<String, ProgramShowTime> function){
        return localCache.get(id,function);
    }
    
    public ProgramShowTime getCache(String id){
        return localCache.getIfPresent(id);
    }
    
    public void del(String id){
        localCache.invalidate(id);
    }
}
