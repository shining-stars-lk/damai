package com.example.model;

import lombok.Data;

@Data
public class ApiStatMethodRelation {
    private String id;
    private String sourceId;
    private String targetId;
    private Double avgRunTime = 0.0;
    private Double maxRunTime = 0.0;
    private Double minRunTime = 10000.0;
    private Long exceptionCount = 0L;
}
