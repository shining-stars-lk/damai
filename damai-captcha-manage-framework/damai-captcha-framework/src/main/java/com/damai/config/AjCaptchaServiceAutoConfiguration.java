package com.damai.config;


import com.damai.captcha.model.common.Const;
import com.damai.captcha.service.CaptchaService;
import com.damai.captcha.service.impl.CaptchaServiceFactory;
import com.damai.captcha.util.ImageUtils;
import com.damai.captcha.util.StringUtils;
import com.damai.properties.AjCaptchaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: AjCaptchaServiceAutoConfiguration
 * @author: 阿星不是程序员
 **/

@Configuration
public class AjCaptchaServiceAutoConfiguration {

    private static Logger logger = LoggerFactory.getLogger(AjCaptchaServiceAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public CaptchaService captchaService(AjCaptchaProperties prop) {
        logger.info("自定义配置项：{}", prop.toString());
        Properties config = new Properties();
        config.put(Const.CAPTCHA_CACHETYPE, prop.getCacheType().name());
        config.put(Const.CAPTCHA_WATER_MARK, prop.getWaterMark());
        config.put(Const.CAPTCHA_FONT_TYPE, prop.getFontType());
        config.put(Const.CAPTCHA_TYPE, prop.getType().getCodeValue());
        config.put(Const.CAPTCHA_INTERFERENCE_OPTIONS, prop.getInterferenceOptions());
        config.put(Const.ORIGINAL_PATH_JIGSAW, prop.getJigsaw());
        config.put(Const.ORIGINAL_PATH_PIC_CLICK, prop.getPicClick());
        config.put(Const.CAPTCHA_SLIP_OFFSET, prop.getSlipOffset());
        config.put(Const.CAPTCHA_AES_STATUS, String.valueOf(prop.getAesStatus()));
        config.put(Const.CAPTCHA_WATER_FONT, prop.getWaterFont());
        config.put(Const.CAPTCHA_CACAHE_MAX_NUMBER, prop.getCacheNumber());
        config.put(Const.CAPTCHA_TIMING_CLEAR_SECOND, prop.getTimingClear());

        config.put(Const.HISTORY_DATA_CLEAR_ENABLE, prop.isHistoryDataClearEnable() ? "1" : "0");

        config.put(Const.REQ_FREQUENCY_LIMIT_ENABLE, prop.getReqFrequencyLimitEnable() ? "1" : "0");
        config.put(Const.REQ_GET_LOCK_LIMIT, String.valueOf(prop.getReqGetLockLimit()));
        config.put(Const.REQ_GET_LOCK_SECONDS, String.valueOf(prop.getReqGetLockSeconds()));
        config.put(Const.REQ_GET_MINUTE_LIMIT, String.valueOf(prop.getReqGetMinuteLimit()));
        config.put(Const.REQ_CHECK_MINUTE_LIMIT, String.valueOf(prop.getReqCheckMinuteLimit()));
        config.put(Const.REQ_VALIDATE_MINUTE_LIMIT, String.valueOf(prop.getReqVerifyMinuteLimit()));

        config.put(Const.CAPTCHA_FONT_SIZE, String.valueOf(prop.getFontSize()));
        config.put(Const.CAPTCHA_FONT_STYLE, String.valueOf(prop.getFontStyle()));
        config.put(Const.CAPTCHA_WORD_COUNT, String.valueOf(prop.getClickWordCount()));

        boolean result1 = StringUtils.isNotBlank(prop.getJigsaw()) && prop.getJigsaw().startsWith("classpath:");
        boolean result2 = StringUtils.isNotBlank(prop.getPicClick()) && prop.getPicClick().startsWith("classpath:");
        if (result1 || result2) {
            //自定义resources目录下初始化底图
            config.put(Const.CAPTCHA_INIT_ORIGINAL, "true");
            initializeBaseMap(prop.getJigsaw(), prop.getPicClick());
        }
        CaptchaService s = CaptchaServiceFactory.getInstance(config);
        return s;
    }

    private static void initializeBaseMap(String jigsaw, String picClick) {
        ImageUtils.cacheBootImage(getResourcesImagesFile(jigsaw + "/original/*.png"),
                getResourcesImagesFile(jigsaw + "/slidingBlock/*.png"),
                getResourcesImagesFile(picClick + "/*.png"));
    }

    public static Map<String, String> getResourcesImagesFile(String path) {
        Map<String, String> imgMap = new HashMap<>(64);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(path);
            for (Resource resource : resources) {
                byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
                String string = Base64.getEncoder().encodeToString(bytes);
                String filename = resource.getFilename();
                imgMap.put(filename, string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgMap;
    }
}
