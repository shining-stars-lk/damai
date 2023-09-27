package com.example.service;


import com.example.model.ExceptionInfo;
import com.example.model.ExceptionNode;
import com.example.model.ExceptionRelation;
import com.example.model.MethodInfo;
import com.example.model.MethodNode;
import com.example.model.MethodRelation;
import com.example.model.ParamMetric;
import com.example.model.SystemStatistic;
import com.example.util.Context;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * zhangchang
 * Note:数据的存储目前是没有考虑线程安全的，实际意义上来说，我们并不需要完整记录每一次调用的结果，因为同一个方法来说，高频调用情况下，每次的耗时差距并不大，
 * 除此之外项目还增加了ko-time.discard-rate参数允许你去丢弃一部分数据，从而提高IO
 * We did not care about thread-safety of all data savers,because it is unnecessary for your actual application
 */
public interface GraphService {

    static GraphService getInstance() {
        return Context.getSaver();
    }

    /**
     * add a method data
     */
    void addMethodNode(MethodNode methodNode);

    /**
     * add the parameters of a method
     */
    void addParamAnalyse(String methodId, Parameter[] names, Object[] values, double v);

    /**
     * add a exception data
     */
    void addExceptionNode(ExceptionNode exceptionNode);

    /**
     * get the method paths
     */
    MethodInfo getTree(String methodId);

    Map<String, ParamMetric> getMethodParamGraph(String methodId);

    SystemStatistic getRunStatistic();

    List<MethodInfo> searchMethods(String question);

    List<MethodInfo> getControllers();

    List<String> getCondidates(String question);

    List<MethodInfo> getChildren(String methodId);

    List<ExceptionInfo> getExceptionInfos(String exceptionId, String message);

    List<ExceptionInfo> getExceptions(String methodId);

    List<ExceptionNode> getExceptions();

    MethodRelation addMethodRelation(MethodNode sourceMethodNode, MethodNode targetMethodNode);

    ExceptionRelation addExceptionRelation(MethodNode sourceMethodNode, ExceptionNode exceptionNode);

    boolean clearAll();

}
