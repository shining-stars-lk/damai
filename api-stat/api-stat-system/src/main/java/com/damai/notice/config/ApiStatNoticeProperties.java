package com.damai.notice.config;

import com.damai.enums.PlatformType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-07
 **/
@Data
@ConfigurationProperties(prefix = ApiStatNoticeProperties.prefix)
public class ApiStatNoticeProperties {
    
    public static final String prefix = "api";
    
    private Integer start = 0;
    
    private Integer end = 10;
    
    private Double min = 0.00;
    
    private Double max = 999999.00;
    
    private boolean enable;
    
    private List<PlatformType> platformTypes;
    
    private PlatformAddressProperties platformAddress;
}
