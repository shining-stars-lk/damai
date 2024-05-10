package com.damai.service.redisstreamconsumer;

import com.damai.MessageConsumer;
import com.damai.core.RedisKeyManage;
import com.damai.redis.RedisKeyBuild;
import com.damai.service.cache.local.LocalCacheProgram;
import com.damai.service.cache.local.LocalCacheProgramGroup;
import com.damai.service.cache.local.LocalCacheProgramShowTime;
import com.damai.service.cache.local.LocalCacheTicketCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.stereotype.Component;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redis-stream消息消费
 * @author: 阿宽不是程序员
 **/
@Slf4j
@Component
public class ProgramRedisStreamConsumer implements MessageConsumer {
    
    @Autowired
    private LocalCacheProgram localCacheProgram;
    
    @Autowired
    private LocalCacheProgramGroup localCacheProgramGroup;
    
    @Autowired
    private LocalCacheProgramShowTime localCacheProgramShowTime;
    
    @Autowired
    private LocalCacheTicketCategory localCacheTicketCategory;
    
    @Override
    public void accept(ObjectRecord<String, String> message) {
        Long programId = Long.parseLong(message.getValue());
        localCacheProgram.del(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM, programId).getRelKey());
        localCacheProgramGroup.del(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_GROUP, programId).getRelKey());
        localCacheProgramShowTime.del(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SHOW_TIME, programId).getRelKey());
        localCacheTicketCategory.del(programId);
    }
}
