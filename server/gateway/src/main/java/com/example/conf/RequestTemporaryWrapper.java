package com.example.conf;

import com.example.common.Result;
import lombok.Data;

import java.util.Map;


@Data
public class RequestTemporaryWrapper {
    
    private Map<String,String> map;
    
    private Result result;
}
