package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目数据操作 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="ProgramOperateDataDto", description ="节目数据操作")
public class ProgramOperateDataDto {
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目id",required = true)
    @NotNull
    private Long programId;
    
    @ApiModelProperty(name ="ticketCategoryCountMap", dataType ="Map<Long,Long>", 
            value ="节目票档map key:票档id value:票档数量",required = true)
    @NotNull
    private Map<Long,Long> ticketCategoryCountMap;
    
    @ApiModelProperty(name ="seatIdList", dataType ="List<Long>", value ="座位id集合",required = true)
    @NotNull
    private List<Long> seatIdList;
    
    @ApiModelProperty(name ="sellStatus", dataType ="Long", value ="座位状态",required = true)
    @NotNull
    private Integer sellStatus;
}
