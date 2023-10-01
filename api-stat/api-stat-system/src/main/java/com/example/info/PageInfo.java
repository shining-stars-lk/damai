package com.example.info;

import lombok.Data;

@Data
public class PageInfo {

    private Long pageNo;

    private Long pageSize;

    private Long totalRecord;

    private Long pageTotal;

    private Long start;

    private Long end;
}
