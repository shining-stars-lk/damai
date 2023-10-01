package com.example.vo;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {

    private Long PageTotal;

    private Long totalRecord;

    private List<T> data;
}
