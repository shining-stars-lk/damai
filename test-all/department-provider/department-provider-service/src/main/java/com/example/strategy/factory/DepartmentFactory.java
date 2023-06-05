package com.example.strategy.factory;

import com.example.enums.BaseCode;
import com.example.exception.ToolkitException;
import com.example.strategy.DepartmentStrategy;

import java.util.HashMap;
import java.util.Optional;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-06-05
 **/
public class DepartmentFactory {

    /**
     * spring执行bean的生命周期函数，如@PostConstruct，是单线程进行的，所以可以使用HashMap
     * */
    private static HashMap<String, DepartmentStrategy> departmentStrategyMap = new HashMap<>();
    
    public static void register(String code, DepartmentStrategy departmentStrategy){
        departmentStrategyMap.put(code,departmentStrategy);
    }
    
    public static DepartmentStrategy getDepartmentStrategy(String code){
        return Optional.ofNullable(departmentStrategyMap.get(code)).orElseThrow(() -> new ToolkitException(BaseCode.DEPARTMENT_STRATEGY));
    }
}
