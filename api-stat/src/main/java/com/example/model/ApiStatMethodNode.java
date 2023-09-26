package com.example.model;

import com.example.util.ApiStatMethodType;
import lombok.Data;

import java.util.Objects;

@Data
public class ApiStatMethodNode {
    private String id;
    private String name;
    private String className;
    private String methodName;
    private String routeName;
    private Double value = 0.0;
    private ApiStatMethodType apiStatMethodType;

    public Double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiStatMethodNode that = (ApiStatMethodNode) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RunTimeNode{" +
                "id='" + id + '\'' +
                ",className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", apiStatMethodType=" + apiStatMethodType +
                ", value=" + value +
                '}';
    }
}
