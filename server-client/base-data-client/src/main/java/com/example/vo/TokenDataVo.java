package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-05
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
