package com.damai.service.cache.local;

import com.damai.util.DateUtils;
import com.damai.vo.ProgramGroupVo;
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
 * @description: 节目分组本地缓存
 * @author: 阿星不是程序员
 **/
@Component
public class LocalCacheProgramGroup {
    
    /**
     * 本地缓存
     * */
    private Cache<String, ProgramGroupVo> localCache;
    
    
    /**
     * 本地缓存的容量
     * */
    @Value("${maximumSize:10000}")
    private Long maximumSize;
    
    @PostConstruct
    public void localLockCacheInit(){
        localCache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfter(new Expiry<String, ProgramGroupVo>() {
                    @Override
                    public long expireAfterCreate(@NonNull final String key, @NonNull final ProgramGroupVo value,
                                                  final long currentTime) {
                        return TimeUnit.MILLISECONDS.toNanos
                                (DateUtils.countBetweenSecond(DateUtils.now(),value.getRecentShowTime()));
                    }
                    
                    @Override
                    public long expireAfterUpdate(@NonNull final String key, @NonNull final ProgramGroupVo value,
                                                  final long currentTime, @NonNegative final long currentDuration) {
                        return currentDuration;
                    }
                    
                    @Override
                    public long expireAfterRead(@NonNull final String key, @NonNull final ProgramGroupVo value,
                                                final long currentTime, @NonNegative final long currentDuration) {
                        return currentDuration;
                    }
                })
                .build();
    }
    
    /**
     * Caffeine的get是线程安全的
     * */
    public ProgramGroupVo getCache(String id, Function<String, ProgramGroupVo> function){
        return localCache.get(id,function);
    }
    
    public void del(String id){
        localCache.invalidate(id);
    }
}
