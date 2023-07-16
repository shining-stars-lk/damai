package com.example.record.conf;

import com.example.record.aspect.RecordAspect;
import com.example.record.es.RecordEsUtils;
import com.example.record.function.IParseFunction;
import com.example.record.function.ParseFunctionFactory;
import com.example.util.BusinessEsUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @program: record
 * @description:
 * @author: 星哥
 * @create: 2023-02-20
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "record.enabled", matchIfMissing = false)
public class RecordConfig {
    
    @Bean
    public RecordEsUtils recordEsUtils(BusinessEsUtil businessEsUtil){
        return new RecordEsUtils(businessEsUtil);
    }
    
    @Bean
    public ParseFunctionFactory parseFunctionFactory(List<IParseFunction> parseFunctions){
        return new ParseFunctionFactory(parseFunctions);
    }
    
    @Bean
    public RecordAspect recordAspect(ParseFunctionFactory parseFunctionFactory,RecordEsUtils recordEsUtils){
        return new RecordAspect(parseFunctionFactory,recordEsUtils);
    }
}
