package com.example.model;

import lombok.Data;

import java.lang.reflect.Parameter;
import java.util.Arrays;

@Data
public class ApiStatInvokedInfo {
    private ApiStatMethodNode current;
    private ApiStatMethodNode parent;
    private ApiStatExceptionNode apiStatExceptionNode;
    Parameter[] names;
    Object[] values;

    @Override
    public String toString() {
        return "ApiStatInvokedInfo{" +
                "current=" + current +
                ", parent=" + parent +
                ", names=" + Arrays.toString(names) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
