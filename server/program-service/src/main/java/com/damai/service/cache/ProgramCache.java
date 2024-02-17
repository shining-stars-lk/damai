package com.damai.service.cache;

import com.damai.dto.ProgramGetDto;
import com.damai.redis.RedisCache;
import com.damai.vo.ProgramVo;
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
