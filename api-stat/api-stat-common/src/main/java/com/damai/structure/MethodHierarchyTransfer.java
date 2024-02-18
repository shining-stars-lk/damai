package com.damai.structure;

import lombok.Data;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
@Data
public class MethodHierarchyTransfer {
    
    private MethodData currentMethodData;
    
    private MethodData parentMethodData;
    
    private boolean exceptionFlag;
    
    Parameter[] names;
    
    Object[] values;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodHierarchyTransfer)) {
            return false;
        }
        MethodHierarchyTransfer that = (MethodHierarchyTransfer) o;
        return isExceptionFlag() == that.isExceptionFlag() && getCurrentMethodData().equals(that.getCurrentMethodData()) && getParentMethodData().equals(that.getParentMethodData()) && Arrays.equals(getNames(), that.getNames()) && Arrays.equals(getValues(), that.getValues());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getCurrentMethodData(), getParentMethodData(), isExceptionFlag());
        result = 31 * result + Arrays.hashCode(getNames());
        result = 31 * result + Arrays.hashCode(getValues());
        return result;
    }
}
