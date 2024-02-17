package com.damai.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-30
 **/
@Data
@ApiModel(value="DepthRuleVo", description ="深度规则")
public class DepthRuleVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="深度规则id", required =true)
    private Long id;
    
    @ApiModelProperty(name ="startTimeWindow", dataType ="String", value ="开始时间窗口", required =true)
    private String startTimeWindow;
    
    @ApiModelProperty(name ="endTimeWindow", dataType ="String", value ="结束时间窗口", required =true)
    private String endTimeWindow;
    
    @ApiModelProperty(name ="statTime", dataType ="Integer", value ="统计时间", required =true)
    private Integer statTime;
    
    @ApiModelProperty(name ="statTimeType", dataType ="Integer", value ="统计时间类型 1:秒 2:分钟", required =true)
    private Integer statTimeType;
    
    @ApiModelProperty(name ="threshold", dataType ="Integer", value ="阈值", required =true)
    private Integer threshold;
    
    @ApiModelProperty(name ="effectiveTime", dataType ="Integer", value ="规则生效限制时间", required =true)
    private Integer effectiveTime;
    
    @ApiModelProperty(name ="effectiveTimeType", dataType ="Integer", value ="规则生效限制时间类型 1:秒 2:分钟", required =true)

    private Integer effectiveTimeType;
    
    private String limitApi;
    
    @ApiModelProperty(name ="message", dataType ="String", value ="提示信息")
    private String message;
    
    @ApiModelProperty(name ="status", dataType ="Integer", value ="状态 1生效 0禁用", required =true)
    private Integer status;
    
    @ApiModelProperty(name ="createTime", dataType ="Date", value ="创建时间", required =true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
