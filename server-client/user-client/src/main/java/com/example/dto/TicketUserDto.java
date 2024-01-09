package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 购票人表
 * </p>
 *
 * @author k
 * @since 2024-01-09
 */
@Data
@ApiModel(value="TicketUserVo", description ="购票人")
public class TicketUserDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ApiModelProperty(name ="userId", dataType ="Long", value ="用户id")
    @NotNull
    private Long userId;
    
    @ApiModelProperty(name ="relName", dataType ="String", value ="用户真实名字")
    @NotBlank
    private String relName;
    
    @ApiModelProperty(name ="idType", dataType ="Integer", value ="证件类型 1:身份证 2:港澳台居民居住证 3:港澳居民来往内地通行证 4:台湾居民来往内地通行证 5:护照 6:外国人永久居住证")
    @NotNull
    private Integer idType;
    
    @ApiModelProperty(name ="idNumber", dataType ="String", value ="证件号码")
    @NotBlank
    private String idNumber;
}
