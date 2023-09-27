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

    public ApiStatMethodRelation addApiStatMethodRelation(ApiStatMethodNode sourceMethodNode, ApiStatMethodNode targetMethodNode, boolean execptionFlag) {
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
        String oldApiStatMethodRelationId = sourceMethodNode.getId() + targetMethodNode.getId();
        ApiStatMethodRelation oldApiStatMethodRelation = methodRelations.get(oldApiStatMethodRelationId);
        if (oldApiStatMethodRelation == null) {
            oldApiStatMethodRelation = new ApiStatMethodRelation();
            oldApiStatMethodRelation.setSourceId(sourceMethodNode.getId());
            oldApiStatMethodRelation.setTargetId(targetMethodNode.getId());
            oldApiStatMethodRelation.setId(oldApiStatMethodRelationId);
            oldApiStatMethodRelation.setAvgRunTime(targetMethodNode.getValue());
            oldApiStatMethodRelation.setMaxRunTime(targetMethodNode.getValue());
            oldApiStatMethodRelation.setMinRunTime(targetMethodNode.getValue());
            oldApiStatMethodRelation.setExceptionCount(execptionFlag ? 1L : 0L);
            methodRelations.put(oldApiStatMethodRelationId,oldApiStatMethodRelation);
        }else {
            BigDecimal bg = BigDecimal.valueOf((targetMethodNode.getValue() + oldApiStatMethodRelation.getAvgRunTime()) / 2.0);
            double avg = bg.setScale(2, RoundingMode.HALF_UP).doubleValue();
            oldApiStatMethodRelation.setAvgRunTime(avg);
            oldApiStatMethodRelation.setMaxRunTime(targetMethodNode.getValue() > oldApiStatMethodRelation.getMaxRunTime() ? targetMethodNode.getValue() : oldApiStatMethodRelation.getMaxRunTime());
            oldApiStatMethodRelation.setMinRunTime(targetMethodNode.getValue() < oldApiStatMethodRelation.getMinRunTime() ? targetMethodNode.getValue() : oldApiStatMethodRelation.getMinRunTime());
            if (execptionFlag) {
                oldApiStatMethodRelation.setExceptionCount(oldApiStatMethodRelation.getExceptionCount() + 1);
            }
        }
        return oldApiStatMethodRelation;
    }
    
    public Map<String, ApiStatMethodNode> getMethodNodes() {
        return methodNodes;
    }
    
    public Map<String, ApiStatMethodRelation> getMethodRelations() {
        return methodRelations;
    }
}
