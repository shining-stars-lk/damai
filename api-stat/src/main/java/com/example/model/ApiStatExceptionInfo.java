package com.example.model;

import lombok.Data;

import java.util.Objects;
@Data
public class ApiStatExceptionInfo {
    private String id;
    private String name;
    private String className;
    private String message;
    private Integer location;
    private String methodName;
    private String occurClassName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiStatExceptionInfo that = (ApiStatExceptionInfo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(className, that.className) &&
                Objects.equals(message, that.message) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, className, message, location);
    }
}
