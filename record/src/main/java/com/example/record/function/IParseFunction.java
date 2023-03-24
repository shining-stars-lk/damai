package com.example.record.function;

public interface IParseFunction<T> {
    
    /**
     * 是否在执行真正的方法前调用此占位符的方法
     * true为是
     * false为否 也就是在执行真正的方法后调用此占位符的方法
     * */
    default boolean executeBefore() {
        return true;
    }
    
    /**
     * 方法名称 和{}中的方法名字相同
     * */
    String functionName();
    
    /**
     * 执行的具体逻辑
     * 入参类型和记录的执行真正的方法入参相同
     * */
    String apply(T t);
}
