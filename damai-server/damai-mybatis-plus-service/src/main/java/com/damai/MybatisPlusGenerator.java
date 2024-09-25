package com.damai;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: mybatisPlus 代码生成器
 * @author: 阿星不是程序员
 **/
public class MybatisPlusGenerator {
    
    public static void main(String[] args) {
        //此为mybatisPlus代码生成器，并不需要启动
        //放在这里的作用是如果自己在工作中需要生成表的对应service、do、实体映射的话，可以参考此配置
        FastAutoGenerator.create("jdbc:mysql://www.cookframe.com:3306/damai?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC&remarks=true&useInformationSchema=true", "root", "root")
                .globalConfig(builder -> {
                    builder.author("k") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
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
                    builder.parent("baomidou") // 设置父包名
                            .moduleName("system") // 设置父包模块名
                            // 设置mapperXml生成路径
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D://baomidou/mybatispluscode")); 
                })
                .strategyConfig(builder -> {
                    // 设置需要生成的表名
                    builder.addInclude("d_user_mobile")
                            // 设置过滤表前缀
                            .addTablePrefix("d_"); 
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine()) 
                .execute();
    }
}
