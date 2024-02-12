package com.example.util;


import com.example.config.RedissonProperties;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;



/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-09
 **/
public class RBloomFilterUtil {
    
    private final RBloomFilter<String> cachePenetrationBloomFilter;
    
    public RBloomFilterUtil(RedissonClient redissonClient, RedissonProperties redissonProperties){
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter(redissonProperties.getRbLoomFilterName());
        cachePenetrationBloomFilter.tryInit(redissonProperties.getExpectedInsertions(), redissonProperties.getFalseProbability());
        this.cachePenetrationBloomFilter = cachePenetrationBloomFilter;
    }
    
    public boolean add(String data) {
        return cachePenetrationBloomFilter.add(data);
    }
    
    public boolean contains(String data) {
        return cachePenetrationBloomFilter.contains(data);
    }
    
    public long getExpectedInsertions() {
        return cachePenetrationBloomFilter.getExpectedInsertions();
    }
    
    public double getFalseProbability() {
        return cachePenetrationBloomFilter.getFalseProbability();
    }
    
    public long getSize() {
        return cachePenetrationBloomFilter.getSize();
    }
    
    public int getHashIterations() {
        return cachePenetrationBloomFilter.getHashIterations();
    }
    
    public long count() {
        return cachePenetrationBloomFilter.count();
    }
}
