package com.example.config;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;


public class JacksonCustom implements Jackson2ObjectMapperBuilderCustomizer, Ordered {

    /** 默认日期时间格式 */
    private final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    /** 默认日期格式 */
    private final String dateFormat = "yyyy-MM-dd";
    /** 默认时间格式 */
    private final String timeFormat = "HH:mm:ss";
    
    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        builder.serializationInclusion(Include.ALWAYS);
        
        //允许单引号、允许不带引号的字段名称
        builder.featuresToEnable(Feature.ALLOW_SINGLE_QUOTES);
        builder.featuresToEnable(Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        
        
        SimpleModule[] simpleModules = new SimpleModule[9];
        
        //添加自定义的序列化器处理空值
        simpleModules[0] = new SimpleModule().setSerializerModifier(new JsonCustomSerializer());
        //将Date类型日期转换为字符串
        simpleModules[1] = new SimpleModule().addSerializer(Date.class, new JsonSerializer<Date>() {

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
                String newValue = sdf.format(value);
                gen.writeString(newValue);
            }

        });
        //将Date类型时间格式转换反序列化
        simpleModules[2] = new SimpleModule().addDeserializer(Date.class, new DateJsonDeserializer());
        //将LocalDateTime进行格式化和序列化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        simpleModules[3] = new SimpleModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        simpleModules[4] = new SimpleModule().addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        //将LocalDate进行格式化和序列化
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
        simpleModules[5] = new SimpleModule().addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        simpleModules[6] = new SimpleModule().addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        //将LocalTime进行格式化和序列化
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timeFormat);
        simpleModules[7] = new SimpleModule().addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        simpleModules[8] = new SimpleModule().addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
        
        builder.modules(simpleModules);
        
        //设置时区
        builder.timeZone(TimeZone.getDefault());
        //允许有未知的属性
        builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //允许包含不带引号的控制字符
        builder.featuresToEnable(Feature.ALLOW_UNQUOTED_CONTROL_CHARS);

        //数字转换为字符串
        builder.featuresToEnable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}