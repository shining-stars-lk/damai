package com.example.structure;

import com.example.enums.MethodLevel;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class MethodDetailData {

    private String id;

    private String className;

    private String methodName;

    private Integer argumentCount;

    private MethodLevel methodLevel;

    private BigDecimal executeTime;

    private String api;

    private BigDecimal avgExecuteTime = new BigDecimal("0");

    private BigDecimal maxExecuteTime = new BigDecimal("0");

    private BigDecimal minExecuteTime = new BigDecimal("0");

    private Long exceptionCount = 0L;

    private List<MethodDetailData> childrenMethodList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodDetailData)) return false;
        MethodDetailData that = (MethodDetailData) o;
        return getId().equals(that.getId()) && getClassName().equals(that.getClassName()) && getMethodName().equals(that.getMethodName()) && Objects.equals(getArgumentCount(), that.getArgumentCount()) && getMethodLevel() == that.getMethodLevel() && Objects.equals(getExecuteTime(), that.getExecuteTime()) && Objects.equals(getApi(), that.getApi()) && Objects.equals(getAvgExecuteTime(), that.getAvgExecuteTime()) && Objects.equals(getMaxExecuteTime(), that.getMaxExecuteTime()) && Objects.equals(getMinExecuteTime(), that.getMinExecuteTime()) && Objects.equals(getExceptionCount(), that.getExceptionCount()) && Objects.equals(getChildrenMethodList(), that.getChildrenMethodList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClassName(), getMethodName(), getArgumentCount(), getMethodLevel(), getExecuteTime(), getApi(), getAvgExecuteTime(), getMaxExecuteTime(), getMinExecuteTime(), getExceptionCount(), getChildrenMethodList());
    }
    
    public BigDecimal getExecuteTime() {
        return executeTime != null ? executeTime.setScale(2, RoundingMode.HALF_UP) : null;
    }
    
    public BigDecimal getAvgExecuteTime() {
        return avgExecuteTime != null ? avgExecuteTime.setScale(2, RoundingMode.HALF_UP) : null;
    }
    
    public BigDecimal getMaxExecuteTime() {
        return maxExecuteTime != null ? maxExecuteTime.setScale(2, RoundingMode.HALF_UP) : null;
    }
    
    public BigDecimal getMinExecuteTime() {
        return minExecuteTime != null ? minExecuteTime.setScale(2, RoundingMode.HALF_UP) : null;
    }
}
