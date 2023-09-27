package com.example.data;


import com.example.model.ApiStatMethodNode;
import com.example.model.ApiStatMethodRelation;
import com.example.util.ApiStatCommon;
import com.example.util.MethodType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;



public class ApiStatMemoryBase {

    private final Map<String, ApiStatMethodNode> methodNodes = new HashMap<>(512);

    private final Map<String, ApiStatMethodRelation> methodRelations = new HashMap<>(512);
    
    
    public void addMethodNode(ApiStatMethodNode apiStatMethodNode) {
        if (null == apiStatMethodNode) {
            return;
        }
        if (!methodNodes.containsKey(apiStatMethodNode.getId())) {
            methodNodes.put(apiStatMethodNode.getId(), apiStatMethodNode);
        } else {
            if (apiStatMethodNode.getMethodType() == MethodType.Controller && !ApiStatCommon.isEmpty(apiStatMethodNode.getRouteName())) {
                ApiStatMethodNode controller = methodNodes.get(apiStatMethodNode.getId());
                if (controller==null) {
                    return;
                }
                controller.setRouteName(apiStatMethodNode.getRouteName());
                methodNodes.put(apiStatMethodNode.getId(), controller);
            }
        }
    }

    public ApiStatMethodRelation addApiStatMethodRelation(ApiStatMethodNode sourceMethodNode, ApiStatMethodNode targetMethodNode) {
        if (null == sourceMethodNode || null == targetMethodNode ) {
            return null;
        }
        if (sourceMethodNode.getId().equals(targetMethodNode.getId())) {
            return null;
        }
        if (targetMethodNode.getMethodType()==MethodType.Controller && !"DispatcherServlet.doDispatch".equals(sourceMethodNode.getName())) {
            return null;
        }
        if (methodRelations.containsKey(targetMethodNode.getId()+sourceMethodNode.getId())) {
            return null;
        }
        ApiStatMethodRelation methodRelation = new ApiStatMethodRelation();
        methodRelation.setSourceId(sourceMethodNode.getId());
        methodRelation.setTargetId(targetMethodNode.getId());
        methodRelation.setId(sourceMethodNode.getId() + targetMethodNode.getId());
        methodRelation.setAvgRunTime(targetMethodNode.getValue());
        methodRelation.setMaxRunTime(targetMethodNode.getValue());
        methodRelation.setMinRunTime(targetMethodNode.getValue());
        ApiStatMethodRelation old = methodRelations.get(methodRelation.getId());
        if (null == old) {
            methodRelations.put(methodRelation.getId(), methodRelation);
            return methodRelation;
        } else {
            BigDecimal bg = BigDecimal.valueOf((methodRelation.getAvgRunTime() + old.getAvgRunTime()) / 2.0);
            double avg = bg.setScale(2, RoundingMode.HALF_UP).doubleValue();
            old.setAvgRunTime(avg);
            old.setMaxRunTime(methodRelation.getMaxRunTime() > old.getMaxRunTime() ? methodRelation.getMaxRunTime() : old.getMaxRunTime());
            old.setMinRunTime(methodRelation.getMinRunTime() < old.getMinRunTime() ? methodRelation.getMinRunTime() : old.getMinRunTime());
            return old;
        }
    }
    
    public Map<String, ApiStatMethodNode> getMethodNodes() {
        return methodNodes;
    }
    
    public Map<String, ApiStatMethodRelation> getMethodRelations() {
        return methodRelations;
    }
}
