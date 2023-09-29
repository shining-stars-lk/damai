package com.example.structure;

import com.example.enums.MethodType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class MethodDetailData {

    private String id;

    private String className;

    private String methodName;

    private Integer argumentCount;

    private MethodType methodType;

    private BigDecimal runTime;

    private String api;

    private BigDecimal avgExecuteTime = new BigDecimal("0");

    private BigDecimal maxExecuteTime = new BigDecimal("0");

    private BigDecimal minExecuteTime = new BigDecimal("0");

    private Long exceptionCount = 0L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodDetailData)) return false;
        MethodDetailData that = (MethodDetailData) o;
        return getId().equals(that.getId()) && getClassName().equals(that.getClassName()) && getMethodName().equals(that.getMethodName()) && Objects.equals(getArgumentCount(), that.getArgumentCount()) && getMethodType() == that.getMethodType() && Objects.equals(getRunTime(), that.getRunTime()) && Objects.equals(getApi(), that.getApi()) && Objects.equals(getAvgExecuteTime(), that.getAvgExecuteTime()) && Objects.equals(getMaxExecuteTime(), that.getMaxExecuteTime()) && Objects.equals(getMinExecuteTime(), that.getMinExecuteTime()) && Objects.equals(getExceptionCount(), that.getExceptionCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClassName(), getMethodName(), getArgumentCount(), getMethodType(), getRunTime(), getApi(), getAvgExecuteTime(), getMaxExecuteTime(), getMinExecuteTime(), getExceptionCount());
    }
}
