package com.example.model;


import com.example.util.ApiStatMethodType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiStatMethodInfo implements Comparable<ApiStatMethodInfo> {
    private String id;
    private String name;
    private String className;
    private String methodName;
    private String routeName;
    private Double value = 0.0;
    private Double avgRunTime = 0.0;
    private Double maxRunTime = 0.0;
    private Double minRunTime = 0.0;
    private ApiStatMethodType apiStatMethodType;
    private Integer exceptionNum = 0;
    private List<ApiStatMethodInfo> children = new ArrayList<>();
    private List<ApiStatExceptionInfo> apiStatExceptionInfos = new ArrayList<>();

    @Override
    public int compareTo(ApiStatMethodInfo o) {
        return -this.avgRunTime.compareTo(o.getAvgRunTime());
    }
    

    @Override
    public String toString() {
        return "RunTimeNode{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodType=" + apiStatMethodType +
                '}';
    }
}
