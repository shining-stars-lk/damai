package com.example.model;

import lombok.Data;

import java.lang.reflect.Parameter;

@Data
public class ApiStatInvokedInfo {
    private ApiStatMethodNode current;
    private ApiStatMethodNode parent;
    Parameter[] names;
    Object[] values;

}
