package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单创建 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="OrderCreateDto", description ="订单创建")
public class OrderCreateDto {
    
    @Schema(name ="orderNumber", type ="Long", description ="订单编号", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long orderNumber;
    
    @Schema(name ="programId", type ="Long", description ="节目表id", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long programId;
    
    @Schema(name ="programItemPicture", type ="String", description ="节目图片介绍", requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String programItemPicture;
    
    @Schema(name ="userId", type ="Long", description ="用户id", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long userId;
    
    @Schema(name ="programTitle", type ="String", description ="节目标题", requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String programTitle;
    
    @Schema(name ="programPlace", type ="String", description ="节目地点", requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String programPlace;
    
    @Schema(name ="programShowTime", type ="Date", description ="节目演出时间", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Date programShowTime;
    
    @Schema(name ="programPermitChooseSeat", type ="Integer", description ="节目是否允许选座 1:允许选座 0:不允许选座", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer programPermitChooseSeat;
    
    @Schema(name ="distributionMode", type ="String", description ="配送方式")
    private String distributionMode;
    
    @Schema(name ="takeTicketMode", type ="String", description ="取票方式")
    private String takeTicketMode;
    
    @Schema(name ="orderPrice", type ="BigDecimal", description ="订单价格", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private BigDecimal orderPrice;
    
    @Schema(name ="createOrderTime", type ="Date", description ="生成订单时间", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Date createOrderTime;
    
    @Schema(name ="orderTicketUserCreateDtoList", type ="List<OrderTicketUserCreateDto>", description ="购票人订单集合", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private List<OrderTicketUserCreateDto> orderTicketUserCreateDtoList;
    
}
