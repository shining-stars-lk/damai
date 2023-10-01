package com.example.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PageVo<T> {

    private Long PageTotal;

    private Long pageNo;

    private Long pageSize;

    private List<T> data;
}
