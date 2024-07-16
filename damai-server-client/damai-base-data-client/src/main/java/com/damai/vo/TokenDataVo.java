package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: token vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="TokenDataVo", description ="token数据")
public class TokenDataVo {
    
    @Schema(name ="id", type ="String", description ="id")
    private Long id;
    
    @Schema(name ="name", type ="String", description ="名称")
    private String name;
    
    @Schema(name ="secret", type ="String", description ="秘钥")
    private String secret;
    
    @Schema(name ="status", type ="Integer", description ="装填 1:正常 0:禁用")
    private Integer status;
    
    @Schema(name ="createTime", type ="Date", description ="创建时间")
    private Date createTime;
}
