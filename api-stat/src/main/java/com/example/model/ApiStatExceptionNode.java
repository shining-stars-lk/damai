package com.example.model;


import lombok.Data;

import java.util.Objects;
@Data
public class ApiStatExceptionNode {
    private String id;
    private String name;
    private String className;
    private String message;
    private Integer value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiStatExceptionNode that = (ApiStatExceptionNode) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(className, that.className) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, className, message);
    }

    @Override
    public String toString() {
        return "ExceptionNode{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", message='" + message + '\'' +
                ", value=" + value +
                '}';
    }
}
