package com.baidu.fsg.uid.config;

import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description : 百度uid集成配置类
 * @Author : huzhiting
 * @Date: 2020-04-23 15:50
 */
@Configuration
public class WorkerNodeConfig {

//    @Bean("disposableWorkerIdAssigner")
//    @ConditionalOnMissingBean(WorkerIdAssigner.class)
//    public WorkerIdAssigner disposableWorkerIdAssigner(){
//        WorkerIdAssigner workerIdAssigner = new DisposableWorkerIdAssigner();
//        return workerIdAssigner;
//    }

    @Bean("cachedUidGenerator")
    public UidGenerator uidGenerator(WorkerIdAssigner disposableWorkerIdAssigner){
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        return cachedUidGenerator;
    }
}