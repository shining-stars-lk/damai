package com.example.config;


import com.example.service.GraphService;
import com.example.util.Context;
import com.example.util.KoUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.logging.Logger;


@Component
public class SaveResourceConfig implements CommandLineRunner {
    private static Logger log = Logger.getLogger(SaveResourceConfig.class.toString());

    @Override
    public void run(String... args) throws Exception {
        DataSource dataSource = KoUtil.getDataSource();
        if (null != dataSource) {
            log.info("kotime=>Setting the finnal DataSource for kotime so that previous DataSources will be invalid.");
            Context.setDataSource(dataSource);
        }
        StringRedisTemplate redisTemplate = KoUtil.getStringRedisTemplate();
        if (null != redisTemplate) {
            log.info("kotime=>Setting the finnal StringRedisTemplate for kotime so that previous StringRedisTemplate will be invalid.");
            Context.setStringRedisTemplate(redisTemplate);
        }

        if (Context.getConfig().getDataReset()) {
            log.info("kotime=>Deleting all data for kotime.");
            GraphService instance = GraphService.getInstance();
            instance.clearAll();
        }

        KoUtil.clearCaches();
    }
}
