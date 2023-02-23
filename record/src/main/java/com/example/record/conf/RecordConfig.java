package com.example.record.conf;

import com.example.record.aspect.RecordAspect;
import com.example.record.es.RecordEsUtils;
import com.example.util.BusinessEsUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: lk
 * @create: 2023-02-20
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "record.enabled", matchIfMissing = false)
public class RecordConfig {
    
    @Bean
    public RecordAspect recordAspect(RecordEsUtils recordEsUtils){
        return new RecordAspect(recordEsUtils);
    }
    
    @Bean
    public RecordEsUtils recordEsUtils(BusinessEsUtil businessEsUtil){
        return new RecordEsUtils(businessEsUtil);
    }
}
