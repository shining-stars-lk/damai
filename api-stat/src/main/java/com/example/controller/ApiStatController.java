package com.example.controller;

import com.example.data.ApiStatMemoryBase;
import com.example.model.ApiStatMethodNode;
import com.example.model.ApiStatMethodRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-27
 **/
@RestController
public class ApiStatController {

    @Autowired
    private ApiStatMemoryBase apiStatMemoryBase;
    
    
    @RequestMapping(value = "getMethodNodes")
    public Map<String, ApiStatMethodNode> getMethodNodes() {
        return apiStatMemoryBase.getMethodNodes();
    }
    
    @RequestMapping(value = "getMethodRelations")
    public Map<String, ApiStatMethodRelation> getMethodRelations() {
        return apiStatMemoryBase.getMethodRelations();
    }
}
