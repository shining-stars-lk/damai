package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 购票人 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="TicketUserInfoVo", description ="购票人数据")
public class TicketUserInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    @Schema(name ="id", type ="Long", description ="购票人id")
    private Long id;
    
    @Schema(name ="userId", type ="Long", description ="用户id")
    private Long userId;
    
    @Schema(name ="relName", type ="String", description ="用户真实名字")
    private String relName;
    
    @Schema(name ="idType", type ="Integer", description ="证件类型 1:身份证 2:港澳台居民居住证 3:港澳居民来往内地通行证 4:台湾居民来往内地通行证 5:护照 6:外国人永久居住证")
    private Integer idType;
    
    @Schema(name ="idNumber", type ="String", description ="证件号码")
    private String idNumber;
}
