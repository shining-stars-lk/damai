package com.damai.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: api调用记录 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ApiDataVo", description ="api被限制调用记录")
public class ApiDataVo {
    
    @Schema(name ="apiAddress", type ="String", description ="api的ip地址")
    private Long id;
    
    private String headVersion;
    
    @Schema(name ="apiAddress", type ="String", description ="api的ip地址")
    private String apiAddress;
    
    @Schema(name ="apiMethod", type ="String", description ="api的方法get post")
    private String apiMethod;
    
    @Schema(name ="apiBody", type ="String", description ="api的请求体")
    private String apiBody;
    
    @Schema(name ="apiParams", type ="String", description ="api的请求参数")
    private String apiParams;
    
    @Schema(name ="apiUrl", type ="String", description ="api路径")
    private String apiUrl;
    
    @Schema(name ="createTime", type ="Date", description ="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @Schema(name ="status", type ="Integer", description ="状态 1生效 0禁用", requiredMode= RequiredMode.REQUIRED)
    private Integer status;
    
    @Schema(name ="callDayTime", type ="String", description ="按天统计")
    private String callDayTime;
    
    @Schema(name ="callHourTime", type ="String", description ="按小时统计")
    private String callHourTime;
    
    @Schema(name ="callMinuteTime", type ="String", description ="按分钟统计")
    private String callMinuteTime;
    
    @Schema(name ="callSecondTime", type ="String", description ="按秒统计")
    private String callSecondTime;
    
}
