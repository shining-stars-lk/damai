package com.example.conf;

import com.example.common.ApiResponse;
import lombok.Data;

import java.util.Map;


@Data
public class RequestTemporaryWrapper {
    
    private Map<String,String> map;
    
    private ApiResponse apiResponse;
}
