package com.example.structure;


import com.example.enums.MethodLevel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
@Data
public class MethodData {
    
    private String id;
    
    private String className;
    
    private String methodName;
    
    private Integer argumentCount;
    
    private MethodLevel methodLevel;
    
    private BigDecimal runTime;
    
    private String api;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodData)) return false;
        MethodData that = (MethodData) o;
        return getId().equals(that.getId()) && getClassName().equals(that.getClassName()) && getMethodName().equals(that.getMethodName()) && Objects.equals(getArgumentCount(), that.getArgumentCount()) && getMethodLevel() == that.getMethodLevel() && Objects.equals(getRunTime(), that.getRunTime()) && Objects.equals(getApi(), that.getApi());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClassName(), getMethodName(), getArgumentCount(), getMethodLevel(), getRunTime(), getApi());
    }
}
