package com.example.config;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class JacksonCustom implements Jackson2ObjectMapperBuilderCustomizer, Ordered {

    /** 默认日期时间格式 */
    private final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        builder.serializationInclusion(Include.ALWAYS);
        
        // 允许单引号、允许不带引号的字段名称
        builder.featuresToEnable(Feature.ALLOW_SINGLE_QUOTES);
        builder.featuresToEnable(Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        
        
        SimpleModule[] simpleModules = new SimpleModule[3];
        
        // 添加自定义的序列化器处理空值
        simpleModules[0] = new SimpleModule().setSerializerModifier(new JsonCustomSerializer());
        //将所有时间都转换为字符串
        simpleModules[1] = new SimpleModule().addSerializer(Date.class, new JsonSerializer<Date>() {

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException, JsonProcessingException {
                SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
                String newValue = sdf.format(value);
                gen.writeString(newValue);
            }

        });
        //时间格式转换反序列化
        simpleModules[2] = new SimpleModule().addDeserializer(Date.class, new DateJsonDeserializer());
        
        builder.modules(simpleModules);
        
        // 设置时区
        builder.timeZone(TimeZone.getDefault());
        
        builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        builder.featuresToEnable(Feature.ALLOW_UNQUOTED_CONTROL_CHARS);

        //数字也加引号
        builder.featuresToEnable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        builder.featuresToEnable(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}