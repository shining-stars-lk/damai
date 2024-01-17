package com.example.composite;

import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CompositeContainer<T> {
    
    private final Map<String, CompositeInterface> allCompositeInterfaceMap = new HashMap<>();
    
    public void add(String type,CompositeInterface compositeInterface){
        allCompositeInterfaceMap.put(type,compositeInterface);
    }
    
    public void execute(String type,T param){
        CompositeInterface compositeInterface = Optional.ofNullable(allCompositeInterfaceMap.get(type))
                .orElseThrow(() -> new CookFrameException(BaseCode.COMPOSITE_NOT_EXIST));
        compositeInterface.batch(param);
    }
}
