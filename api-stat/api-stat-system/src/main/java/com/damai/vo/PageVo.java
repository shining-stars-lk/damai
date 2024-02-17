package com.damai.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVo<T> {

    private Long PageTotal;

    private Long pageNo;

    private Long pageSize;

    private List<T> data;
}
