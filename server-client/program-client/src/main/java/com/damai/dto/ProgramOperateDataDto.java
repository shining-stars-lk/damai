package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-26
 **/
@Data
@ApiModel(value="ProgramOperateDataDto", description ="节目数据操作")
public class ProgramOperateDataDto {
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目id")
    @NotNull
    private Long programId;
    
    @ApiModelProperty(name ="ticketCategoryCountMap", dataType ="Map<Long,Long>", value ="节目票档map key:票档id value:票档数量")
    @NotNull
    private Map<Long,Long> ticketCategoryCountMap;
    
    @ApiModelProperty(name ="seatIdList", dataType ="List<Long>", value ="座位id集合")
    @NotNull
    private List<Long> seatIdList;
    
    @ApiModelProperty(name ="sellStatus", dataType ="Long", value ="座位状态")
    @NotNull
    private Integer sellStatus;
}
