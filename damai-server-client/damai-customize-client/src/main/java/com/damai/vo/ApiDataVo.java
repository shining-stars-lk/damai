package com.damai.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: api调用记录 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="ApiDataVo", description ="api被限制调用记录")
public class ApiDataVo {
    
    @ApiModelProperty(name ="apiAddress", dataType ="String", value ="api的ip地址")
    private Long id;
    
    private String headVersion;
    
    @ApiModelProperty(name ="apiAddress", dataType ="String", value ="api的ip地址")
    private String apiAddress;
    
    @ApiModelProperty(name ="apiMethod", dataType ="String", value ="api的方法get post")
    private String apiMethod;
    
    @ApiModelProperty(name ="apiBody", dataType ="String", value ="api的请求体")
    private String apiBody;
    
    @ApiModelProperty(name ="apiParams", dataType ="String", value ="api的请求参数")
    private String apiParams;
    
    @ApiModelProperty(name ="apiUrl", dataType ="String", value ="api路径")
    private String apiUrl;
    
    @ApiModelProperty(name ="createTime", dataType ="Date", value ="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @ApiModelProperty(name ="status", dataType ="Integer", value ="状态 1生效 0禁用", required =true)
    private Integer status;
    
    @ApiModelProperty(name ="callDayTime", dataType ="String", value ="按天统计")
    private String callDayTime;
    
    @ApiModelProperty(name ="callHourTime", dataType ="String", value ="按小时统计")
    private String callHourTime;
    
    @ApiModelProperty(name ="callMinuteTime", dataType ="String", value ="按分钟统计")
    private String callMinuteTime;
    
    @ApiModelProperty(name ="callSecondTime", dataType ="String", value ="按秒统计")
    private String callSecondTime;
    
}
