package com.example.handler;


import com.example.model.ExceptionNode;
import com.example.model.MethodNode;

import java.lang.reflect.Parameter;

/**
 * zhangchang
 */
public interface InvokedHandler {
    /**
     * return the results after invoking a method.
     */
    void onInvoked(MethodNode current, MethodNode parent, Parameter[] names, Object[] values);
    /**
     * return the results with the possible exception after invoking a method.
     */
    default void onException(MethodNode current, MethodNode parent, ExceptionNode exception, Parameter[] names, Object[] values){

    }
}
