package com.example.model;

import lombok.Data;

import java.lang.reflect.Parameter;

@Data
public class ApiStatInvokedInfo {
    private ApiStatMethodNode current;
    private ApiStatMethodNode parent;
    private boolean exceptionFlag;
    Parameter[] names;
    Object[] values;

}
