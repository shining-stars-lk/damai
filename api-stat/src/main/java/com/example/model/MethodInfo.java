package com.example.model;


import com.example.util.MethodType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class   MethodInfo implements Comparable<MethodInfo> {
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
    private Integer exceptionNum = 0;
    private List<MethodInfo> children = new ArrayList<>();
    private List<ExceptionInfo> exceptions = new ArrayList<>();

    @Override
    public int compareTo(MethodInfo o) {
        return -this.avgRunTime.compareTo(o.getAvgRunTime());
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public List<ExceptionInfo> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<ExceptionInfo> exceptions) {
        this.exceptions = exceptions;
    }

    public Integer getExceptionNum() {
        return exceptionNum;
    }

    public void setExceptionNum(Integer exceptionNum) {
        this.exceptionNum = exceptionNum;
    }

    public Double getValue() {
        return value;
    }

    public Double getAvgRunTime() {
        return avgRunTime;
    }

    public void setAvgRunTime(Double avgRunTime) {
        this.avgRunTime = avgRunTime;
    }

    public Double getMaxRunTime() {
        return maxRunTime;
    }

    public void setMaxRunTime(Double maxRunTime) {
        this.maxRunTime = maxRunTime;
    }

    public Double getMinRunTime() {
        return minRunTime;
    }

    public void setMinRunTime(Double minRunTime) {
        this.minRunTime = minRunTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodInfo that = (MethodInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public List<MethodInfo> getChildren() {
        return children;
    }

    public void setChildren(List<MethodInfo> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "RunTimeNode{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodType=" + methodType +
                '}';
    }
}
