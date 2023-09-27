package com.example.model;

import com.example.util.MethodType;
import lombok.Data;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-27
 **/
@Data
public class ApiStatMethodNode {
    
    private String id;
    private String name;
    private String className;
    private String methodName;
    private String routeName;
    private Double value = 0.0;
    private MethodType methodType;
}
