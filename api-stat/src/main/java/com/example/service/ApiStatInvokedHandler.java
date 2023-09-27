package com.example.service;


import com.example.data.ApiStatMemoryBase;
import com.example.model.ApiStatInvokedInfo;
import com.example.model.ApiStatMethodNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ApiStatInvokedHandler  {
    
    private final ApiStatMemoryBase apiStatMemoryBase;
    
    public void onInvoked(ApiStatInvokedInfo apiStatInvokedInfo) {
        if (apiStatInvokedInfo == null) {
            return;
        }
        ApiStatMethodNode parent = apiStatInvokedInfo.getParent();
        ApiStatMethodNode current = apiStatInvokedInfo.getCurrent();
        apiStatMemoryBase.addMethodNode(parent);
        apiStatMemoryBase.addMethodNode(current);
        apiStatMemoryBase.addApiStatMethodRelation(parent, current, apiStatInvokedInfo.isExceptionFlag());
    }
}
