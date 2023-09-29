package com.example.structure;

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
public class MethodHierarchy {

    private String id;
    
    private String parentMethodDataId;
    
    private String currentMethodDataId;
    
    private BigDecimal avgExecuteTime = new BigDecimal("0");
    
    private BigDecimal maxExecuteTime = new BigDecimal("0");
    
    private BigDecimal minExecuteTime = new BigDecimal("0");
    
    private Long exceptionCount = 0L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodHierarchy)) return false;
        MethodHierarchy that = (MethodHierarchy) o;
        return getId().equals(that.getId()) && getParentMethodDataId().equals(that.getParentMethodDataId()) && getCurrentMethodDataId().equals(that.getCurrentMethodDataId()) && Objects.equals(getAvgExecuteTime(), that.getAvgExecuteTime()) && Objects.equals(getMaxExecuteTime(), that.getMaxExecuteTime()) && Objects.equals(getMinExecuteTime(), that.getMinExecuteTime()) && Objects.equals(getExceptionCount(), that.getExceptionCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getParentMethodDataId(), getCurrentMethodDataId(), getAvgExecuteTime(), getMaxExecuteTime(), getMinExecuteTime(), getExceptionCount());
    }
}
