package com.example.model;

import java.lang.reflect.Parameter;
import java.util.Arrays;

public class InvokedInfo {
    private MethodNode current;
    private MethodNode parent;
    private ExceptionNode exception;
    Parameter[] names;
    Object[] values;

    public ExceptionNode getException() {
        return exception;
    }

    public void setException(ExceptionNode exception) {
        this.exception = exception;
    }

    public Parameter[] getNames() {
        return names;
    }

    public void setNames(Parameter[] names) {
        this.names = names;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public MethodNode getCurrent() {
        return current;
    }

    public void setCurrent(MethodNode current) {
        this.current = current;
    }

    public MethodNode getParent() {
        return parent;
    }

    public void setParent(MethodNode parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "InvokedInfo{" +
                "current=" + current +
                ", parent=" + parent +
                ", names=" + Arrays.toString(names) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
