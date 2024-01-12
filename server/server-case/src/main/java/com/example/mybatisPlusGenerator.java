package com.example;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class mybatisPlusGenerator {
    
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://82.157.255.105:3306/damai_order?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC&remarks=true&useInformationSchema=true", "root", "qaz321qwe!")
                .globalConfig(builder -> {
                    builder.author("k") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D://"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    if (typeCode == Types.TINYINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER; 
                    }
                    return typeRegistry.getColumnType(metaInfo);
                    
                }))
                .packageConfig(builder -> {
                    builder.parent("com.baomidou.mybatisplus.samples.generator") // 设置父包名
                            .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D://mybatispluscode")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("d_order_ticket_user") // 设置需要生成的表名
                            .addTablePrefix("d_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
