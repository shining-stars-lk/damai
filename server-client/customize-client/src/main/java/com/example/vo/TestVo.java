package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-30
 **/
@Data
@ApiModel(value="TestVo", description ="测试")
public class TestVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="id")
    @NotNull
    private Long id1;
    
    @ApiModelProperty(name ="id2", dataType ="Integer", value ="id2")
    private Integer id2;
    
    @ApiModelProperty(name ="id3", dataType ="BigDecimal", value ="id3")
    private BigDecimal id3;
    
    @ApiModelProperty(name ="time1", dataType ="Date", value ="time1")
    private Date time1;
    
    @ApiModelProperty(name ="localDateTime", dataType ="LocalDateTime", value ="localDateTime")
    private LocalDateTime localDateTime;
    
    @ApiModelProperty(name ="localDate", dataType ="LocalDate", value ="localDate")
    private LocalDate localDate;
    
    @ApiModelProperty(name ="localTime", dataType ="LocalTime", value ="localTime")
    private LocalTime localTime;
}
