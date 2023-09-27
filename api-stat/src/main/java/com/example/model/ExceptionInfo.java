package com.example.model;

import java.util.Objects;

public class ExceptionInfo {
    private String id;
    private String name;
    private String className;
    private String message;
    private Integer location;
    private String methodName;
    private String occurClassName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getOccurClassName() {
        return occurClassName;
    }

    public void setOccurClassName(String occurClassName) {
        this.occurClassName = occurClassName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionInfo that = (ExceptionInfo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(className, that.className) &&
                Objects.equals(message, that.message) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, className, message, location);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }


}
