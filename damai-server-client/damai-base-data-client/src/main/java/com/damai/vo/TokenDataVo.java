package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: token vo
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="TokenDataVo", description ="token数据")
public class TokenDataVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="id", required =true)
    private Long id;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="名称", required =true)
    private String name;
    
    @ApiModelProperty(name ="secret", dataType ="String", value ="秘钥", required =true)
    private String secret;
    
    @ApiModelProperty(name ="status", dataType ="Integer", value ="装填 1:正常 0:禁用", required =true)
    private Integer status;
    
    @ApiModelProperty(name ="createTime", dataType ="Date", value ="创建时间", required =true)
    private Date createTime;
}
