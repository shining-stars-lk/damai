package com.example.service;


import com.example.data.ApiStatMemoryBase;
import com.example.model.ApiStatMethodNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;

@Slf4j
@AllArgsConstructor
public class ApiStatInvokedHandler  {
    
    private final ApiStatMemoryBase apiStatMemoryBase;
    
    public void onInvoked(ApiStatMethodNode current, ApiStatMethodNode parent, Parameter[] names, Object[] values) {
        if (current == null || (current != null && current.getValue() == 0.0)) {
            return;
        }
        apiStatMemoryBase.addMethodNode(parent);
        apiStatMemoryBase.addMethodNode(current);
        apiStatMemoryBase.addApiStatMethodRelation(parent, current);
    }
}
