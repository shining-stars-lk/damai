package com.example.model;


import java.util.Objects;

public class ExceptionNode {
    private String id;
    private String name;
    private String className;
    private String message;
    private Integer value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionNode that = (ExceptionNode) o;
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
