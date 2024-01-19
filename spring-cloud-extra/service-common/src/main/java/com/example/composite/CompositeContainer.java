package com.example.composite;

import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CompositeContainer<T> {
    
    private final Map<String, AbstractComposite> allCompositeInterfaceMap = new HashMap<>();
    
    public void add(String type, AbstractComposite abstractComposite){
        allCompositeInterfaceMap.put(type, abstractComposite);
    }
    
    public void execute(String type,T param){
        AbstractComposite compositeInterface = Optional.ofNullable(allCompositeInterfaceMap.get(type))
                .orElseThrow(() -> new CookFrameException(BaseCode.COMPOSITE_NOT_EXIST));
        compositeInterface.allExecute(param);
    }
}
