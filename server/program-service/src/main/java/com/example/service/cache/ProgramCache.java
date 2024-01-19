package com.example.service.cache;

import com.example.dto.ProgramGetDto;
import com.example.redis.RedisCache;
import com.example.vo.ProgramVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-19
 **/
public class ProgramCache {
    
    @Autowired
    private RedisCache redisCache;
    
    public ProgramVo getById(ProgramGetDto programGetDto){
        return null;
    }
    
}
