package com.baidu.fsg.uid.config;

import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import com.damai.toolkit.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 百度uid集成配置类
 * @author: 阿星不是程序员
 **/
@Configuration
public class WorkerNodeConfig {

//    @Bean("disposableWorkerIdAssigner")
//    @ConditionalOnMissingBean(WorkerIdAssigner.class)
//    public WorkerIdAssigner disposableWorkerIdAssigner(){
//        WorkerIdAssigner workerIdAssigner = new DisposableWorkerIdAssigner();
//        return workerIdAssigner;
//    }

    @Bean("cachedUidGenerator")
    public UidGenerator uidGenerator(WorkerIdAssigner disposableWorkerIdAssigner, SnowflakeIdGenerator snowflakeIdGenerator){
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        cachedUidGenerator.setSnowflakeIdGenerator(snowflakeIdGenerator);
        return cachedUidGenerator;
    }
}