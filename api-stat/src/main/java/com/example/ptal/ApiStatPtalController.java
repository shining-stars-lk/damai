package com.example.ptal;

import com.example.data.ApiStatMemoryBase;
import com.example.ptal.model.ApiStatMethodInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-27
 **/
@RestController
public class ApiStatPtalController {
    
    @Autowired
    private ApiStatMemoryBase apiStatMemoryBase;
    
    
    
    @PostMapping(value = "getApis") 
    public List<ApiStatMethodInfo> getApis(){
        return null;
    }
}
