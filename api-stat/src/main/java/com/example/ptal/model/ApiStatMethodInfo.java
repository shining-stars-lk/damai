package com.example.ptal.model;


import com.example.util.MethodType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiStatMethodInfo {
    private String id;
    private String name;
    private String className;
    private String methodName;
    private String routeName;
    private Double value = 0.0;
    private Double avgRunTime = 0.0;
    private Double maxRunTime = 0.0;
    private Double minRunTime = 0.0;
    private MethodType methodType;
    private Long exceptionCount = 0L;
    private List<ApiStatMethodInfo> children = new ArrayList<>();
}
