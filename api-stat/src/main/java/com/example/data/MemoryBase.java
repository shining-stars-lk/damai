package com.example.data;


import com.example.model.ExceptionInfo;
import com.example.model.ExceptionNode;
import com.example.model.ExceptionRelation;
import com.example.model.MethodInfo;
import com.example.model.MethodNode;
import com.example.model.MethodRelation;
import com.example.model.ParamMetric;
import com.example.model.SystemStatistic;
import com.example.service.GraphService;
import com.example.util.Common;
import com.example.util.Context;
import com.example.util.MethodType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * zhangchang
 */
@Component("memory")
public class MemoryBase implements GraphService {

    private volatile static Map<String, MethodNode> methodNodes;

    private volatile static Map<String, ExceptionNode> exceptions;

    private volatile static Map<String, MethodRelation> methodRelations;

    private volatile static Map<String, ExceptionRelation> exceptionRelations;

    private volatile static Map<String, Map<String, ParamMetric>> paramValueMetricMap;

    static {
        methodNodes = new HashMap<>();
        exceptions = new HashMap<>();
        methodRelations = new HashMap<>();
        exceptionRelations = new HashMap<>();
        paramValueMetricMap = new HashMap<String, Map<String, ParamMetric>>();
    }

    @Override
    public void addMethodNode(MethodNode methodNode) {
        if (null == methodNode) {
            return;
        }
        if (!methodNodes.containsKey(methodNode.getId())) {
            methodNodes.put(methodNode.getId(), methodNode);
        } else {
            if (methodNode.getMethodType() == MethodType.Controller && !Common.isEmpty(methodNode.getRouteName())) {
                MethodNode controller = methodNodes.get(methodNode.getId());
                if (controller==null) {
                    return;
                }
                controller.setRouteName(methodNode.getRouteName());
                methodNodes.put(methodNode.getId(), controller);
            }
        }
    }

    @Override
    public void addParamAnalyse(String methodId, Parameter[] names, Object[] values, double v) {
        String paramsKey = Common.getPramsStr(names,values);
        if (paramValueMetricMap.containsKey(methodId)) {
            Map<String, ParamMetric> paramMetricMap = paramValueMetricMap.get(methodId);
            if (paramMetricMap.containsKey(paramsKey)) {
                if (Math.random()< Context.getConfig().getDiscardRate()) {
                    return;
                }
                ParamMetric paramMetric = paramMetricMap.get(paramsKey);
                BigDecimal bg = BigDecimal.valueOf((paramMetric.getAvgRunTime() + v) / 2.0);
                double avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                paramMetric.setAvgRunTime(avg);
                if (v > paramMetric.getMaxRunTime()) {
                    paramMetric.setMaxRunTime(v);
                }
                if (v < paramMetric.getMinRunTime()) {
                    paramMetric.setMinRunTime(v);
                }
            } else {
                ParamMetric paramMetric = new ParamMetric();
                paramMetric.setMaxRunTime(v);
                paramMetric.setAvgRunTime(v);
                paramMetric.setMaxRunTime(v);
                paramMetricMap.put(paramsKey, paramMetric);
            }
        } else {
            ParamMetric paramMetric = new ParamMetric();
            paramMetric.setMaxRunTime(v);
            paramMetric.setAvgRunTime(v);
            paramMetric.setMaxRunTime(v);

            Map<String, ParamMetric> paramMetricMap = new HashMap<>();
            paramMetricMap.put(paramsKey, paramMetric);
            paramValueMetricMap.put(methodId, paramMetricMap);
        }
    }


    @Override
    public MethodRelation addMethodRelation(MethodNode sourceMethodNode, MethodNode targetMethodNode) {
        if (null == sourceMethodNode || null == targetMethodNode ) {
            return null;
        }
        if (sourceMethodNode.getId().equals(targetMethodNode.getId())) {
            return null;
        }
        if (targetMethodNode.getMethodType()==MethodType.Controller && !"Controller.dispatch".equals(sourceMethodNode.getName())) {
            return null;
        }
        if (methodRelations.containsKey(targetMethodNode.getId()+sourceMethodNode.getId())) {
            return null;
        }
        MethodRelation methodRelation = new MethodRelation();
        methodRelation.setSourceId(sourceMethodNode.getId());
        methodRelation.setTargetId(targetMethodNode.getId());
        methodRelation.setId(sourceMethodNode.getId() + targetMethodNode.getId());
        methodRelation.setAvgRunTime(targetMethodNode.getValue());
        methodRelation.setMaxRunTime(targetMethodNode.getValue());
        methodRelation.setMinRunTime(targetMethodNode.getValue());
        MethodRelation old = methodRelations.get(methodRelation.getId());
        if (null == old) {
            methodRelations.put(methodRelation.getId(), methodRelation);
            return methodRelation;
        } else {
            if (Math.random()<Context.getConfig().getDiscardRate()) {
                return null;
            }
            BigDecimal bg = BigDecimal.valueOf((methodRelation.getAvgRunTime() + old.getAvgRunTime()) / 2.0);
            double avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            old.setAvgRunTime(avg);
            old.setMaxRunTime(methodRelation.getMaxRunTime() > old.getMaxRunTime() ? methodRelation.getMaxRunTime() : old.getMaxRunTime());
            old.setMinRunTime(methodRelation.getMinRunTime() < old.getMinRunTime() ? methodRelation.getMinRunTime() : old.getMinRunTime());
            return old;
        }
    }

    @Override
    public ExceptionRelation addExceptionRelation(MethodNode sourceMethodNode, ExceptionNode exceptionNode) {
        String id = sourceMethodNode.getId()+exceptionNode.getId()+exceptionNode.getMessage()+exceptionNode.getValue();

        ExceptionRelation exceptionRelation = new ExceptionRelation();
        exceptionRelation.setId(id);
        exceptionRelation.setSourceId(sourceMethodNode.getId());
        exceptionRelation.setTargetId(exceptionNode.getId());
        exceptionRelation.setLocation(exceptionNode.getValue());
        exceptionRelation.setMessage(exceptionNode.getMessage());
        ExceptionRelation old = exceptionRelations.get(exceptionRelation.getId());
        if (null == old) {
            exceptionRelations.put(exceptionRelation.getId(), exceptionRelation);
            return exceptionRelation;
        } else {
            return old;
        }
    }

    @Override
    public void addExceptionNode(ExceptionNode exceptionNode) {
        if (!exceptions.containsKey(exceptionNode.getId())) {
            exceptions.put(exceptionNode.getId(), exceptionNode);
        }
    }

    @Override
    public List<ExceptionInfo> getExceptions(String methodId) {
        List<ExceptionInfo> exceptionInfos = new ArrayList<>();
        List<ExceptionRelation> relations = exceptionRelations.values().stream().filter(exceptionRelation -> exceptionRelation.getSourceId().equals(methodId)).collect(toList());
        for (ExceptionRelation relation : relations) {
            String exceptionId = relation.getTargetId();
            ExceptionNode exceptionNode = exceptions.get(exceptionId);
            ExceptionInfo exceptionInfo = new ExceptionInfo();
            exceptionInfo.setId(exceptionNode.getId());
            exceptionInfo.setName(exceptionNode.getName());
            exceptionInfo.setClassName(exceptionNode.getClassName());
            exceptionInfo.setMessage(relation.getMessage());
            exceptionInfo.setLocation(relation.getLocation());
            if (!exceptionInfos.contains(exceptionInfo)) {
                exceptionInfos.add(exceptionInfo);
            }
        }
        return exceptionInfos;
    }

    @Override
    public List<ExceptionInfo> getExceptionInfos(String exceptionId,String message) {
        List<ExceptionInfo> exceptionInfos = new ArrayList<>();
        for (ExceptionRelation relation : exceptionRelations.values()) {
            if (relation.getTargetId().equals(exceptionId) && relation.getMessage().equals(message)) {
                String sourceMethodId = relation.getSourceId();
                MethodNode methodNode = methodNodes.get(sourceMethodId);
                ExceptionNode exceptionNode = exceptions.get(exceptionId);

                ExceptionInfo exceptionInfo = new ExceptionInfo();
                exceptionInfo.setId(exceptionNode.getId());
                exceptionInfo.setName(exceptionNode.getName());
                exceptionInfo.setClassName(exceptionNode.getClassName());
                exceptionInfo.setLocation(relation.getLocation());
                exceptionInfo.setMessage(relation.getMessage());
                exceptionInfo.setMethodName(methodNode.getMethodName());
                exceptionInfo.setOccurClassName(methodNode.getClassName());
                if (!exceptionInfos.contains(exceptionInfo)) {
                    exceptionInfos.add(exceptionInfo);
                }
            }
        }
        return exceptionInfos;
    }

    @Override
    public List<ExceptionNode> getExceptions() {
        List<ExceptionNode> exceptionInfos = new ArrayList<>();
        for (ExceptionNode exceptionNode : exceptions.values()) {
            List<ExceptionRelation> relations = exceptionRelations.values().stream().filter(relation -> relation.getTargetId().equals(exceptionNode.getId())).collect(toList());
            for (ExceptionRelation relation : relations) {
                ExceptionNode re = new ExceptionNode();
                re.setId(exceptionNode.getId());
                re.setName(exceptionNode.getName());
                re.setClassName(exceptionNode.getClassName());
                re.setMessage(relation.getMessage());
                re.setValue(relation.getLocation());
                if (!exceptionInfos.contains(re)) {
                    exceptionInfos.add(re);
                }
            }
        }
        return exceptionInfos;
    }

    @Override
    public List<MethodInfo> getControllers() {
        List<MethodInfo> methodInfos = new ArrayList<>();
        for (MethodNode methodNode : methodNodes.values()) {
            if (MethodType.Controller == methodNode.getMethodType()) {
                String id = methodNode.getId();
                Optional<MethodRelation> relations = methodRelations.values().stream().filter(methodRelation -> methodRelation.getTargetId().equals(id)).findFirst();
                MethodRelation relation = null;
                if (relations.isPresent()) {
                    relation = relations.get();
                } else {
                    continue;
                }
                MethodInfo methodInfo = new MethodInfo();
                methodInfo.setId(methodNode.getId());
                methodInfo.setName(methodNode.getName());
                methodInfo.setClassName(methodNode.getClassName());
                methodInfo.setMethodName(methodNode.getMethodName());
                methodInfo.setMethodType(methodNode.getMethodType());
                methodInfo.setRouteName(methodNode.getRouteName());
                methodInfo.setValue(relation.getAvgRunTime());
                methodInfo.setAvgRunTime(relation.getAvgRunTime());
                methodInfo.setMaxRunTime(relation.getMaxRunTime());
                methodInfo.setMinRunTime(relation.getMinRunTime());
                if (!methodInfos.contains(methodInfo)) {
                    methodInfos.add(methodInfo);
                }
            }
        }
        return methodInfos;
    }

    @Override
    public Map<String, ParamMetric> getMethodParamGraph(String methodId) {
        Map<String, ParamMetric> paramMetricMap = paramValueMetricMap.get(methodId);
        return paramMetricMap;
    }

    @Override
    public List<MethodInfo> searchMethods(String question) {
        List<MethodInfo> methodInfos = new ArrayList<>();
        for (MethodNode methodNode : methodNodes.values()) {
            if (methodNode.getName().toLowerCase().contains(question.toLowerCase())) {
                String id = methodNode.getId();
                Optional<MethodRelation> relations = methodRelations.values().stream().filter(methodRelation -> methodRelation.getTargetId().equals(id)).findFirst();
                MethodRelation relation = null;
                if (relations.isPresent()) {
                    relation = relations.get();
                } else {
                    continue;
                }
                MethodInfo methodInfo = new MethodInfo();
                methodInfo.setId(methodNode.getId());
                methodInfo.setName(methodNode.getName());
                methodInfo.setClassName(methodNode.getClassName());
                methodInfo.setMethodName(methodNode.getMethodName());
                methodInfo.setMethodType(methodNode.getMethodType());
                methodInfo.setRouteName(methodNode.getRouteName());
                methodInfo.setValue(relation.getAvgRunTime());
                methodInfo.setAvgRunTime(relation.getAvgRunTime());
                methodInfo.setMaxRunTime(relation.getMaxRunTime());
                methodInfo.setMinRunTime(relation.getMinRunTime());
                if (!methodInfos.contains(methodInfo)) {
                    methodInfos.add(methodInfo);
                }
            }
        }
        return methodInfos;
    }

    @Override
    public List<String> getCondidates(String question) {
        List<String> methodInfos = new ArrayList<>();
        for (MethodNode methodNode : methodNodes.values()) {
            if (methodNode.getName().toLowerCase().contains(question.toLowerCase())) {
                if (!methodInfos.contains(methodNode.getName())) {
                    methodInfos.add(methodNode.getName());
                }
            }
            if (methodInfos.size() >= 10) {
                break;
            }
        }
        return methodInfos;
    }



    @Override
    public List<MethodInfo> getChildren(String methodId) {
        List<MethodInfo> methodInfos = new ArrayList<>();
        for (MethodRelation methodRelation : methodRelations.values()) {
            if (methodRelation.getSourceId().equals(methodId)) {
                String targetMethodId = methodRelation.getTargetId();
                MethodNode methodNode = methodNodes.get(targetMethodId);
                MethodInfo methodInfo = new MethodInfo();
                methodInfo.setId(methodNode.getId());
                methodInfo.setName(methodNode.getName());
                methodInfo.setClassName(methodNode.getClassName());
                methodInfo.setMethodName(methodNode.getMethodName());
                methodInfo.setRouteName(methodNode.getRouteName());
                methodInfo.setMethodType(methodNode.getMethodType());
                methodInfo.setValue(methodRelation.getAvgRunTime());
                methodInfo.setAvgRunTime(methodRelation.getAvgRunTime());
                methodInfo.setMaxRunTime(methodRelation.getMaxRunTime());
                methodInfo.setMinRunTime(methodRelation.getMinRunTime());

                List<ExceptionInfo> exceptionInfos = getExceptions(methodNode.getId());
                methodInfo.setExceptionNum(exceptionInfos.size());
                methodInfo.setExceptions(exceptionInfos);
                if (!methodInfos.contains(methodInfo)) {
                    methodInfos.add(methodInfo);
                }
            }
        }
        return methodInfos;
    }

    public SystemStatistic getRunStatistic() {
        SystemStatistic systemStatistic = new SystemStatistic();
        List<MethodInfo> controllerApis = getControllers();
        if (null == controllerApis || controllerApis.size() == 0) {
            return systemStatistic;
        }
        int delayNum = (int) controllerApis.stream().filter(controllerApi -> controllerApi.getAvgRunTime() >= Context.getConfig().getThreshold()).count();
        systemStatistic.setDelayNum(delayNum);
        int normalNum = (int) controllerApis.stream().filter(controllerApi -> controllerApi.getAvgRunTime() < Context.getConfig().getThreshold()).count();
        systemStatistic.setNormalNum(normalNum);
        int totalNum = (int) controllerApis.stream().count();
        systemStatistic.setTotalNum(totalNum);
        Double max = controllerApis.stream().map(api -> api.getAvgRunTime()).max(Double::compareTo).get();
        Double min = controllerApis.stream().map(api -> api.getAvgRunTime()).min(Double::compareTo).get();
        Double avg = controllerApis.stream().map(api -> api.getAvgRunTime()).collect(Collectors.averagingDouble(Double::doubleValue));
        BigDecimal bg = BigDecimal.valueOf(avg);
        avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        systemStatistic.setMaxRunTime(max);
        systemStatistic.setMinRunTime(min);
        systemStatistic.setAvgRunTime(avg);
        return systemStatistic;
    }

    @Override
    public MethodInfo getTree(String methodId) {
        MethodInfo rootInfo = new MethodInfo();
        MethodNode methodNode = methodNodes.get(methodId);
        rootInfo.setId(methodNode.getId());
        rootInfo.setName(methodNode.getName());
        rootInfo.setClassName(methodNode.getClassName());
        rootInfo.setMethodName(methodNode.getMethodName());
        rootInfo.setMethodType(methodNode.getMethodType());
        rootInfo.setRouteName(methodNode.getRouteName());
        MethodRelation methodRelation = methodRelations.values().stream().filter(relation -> relation.getTargetId().equals(methodId)).findFirst().get();
        rootInfo.setValue(methodRelation.getAvgRunTime());
        rootInfo.setAvgRunTime(methodRelation.getAvgRunTime());
        rootInfo.setMaxRunTime(methodRelation.getMaxRunTime());
        rootInfo.setMinRunTime(methodRelation.getMinRunTime());
        List<ExceptionInfo> exceptionInfos = getExceptions(methodId);
        rootInfo.setExceptionNum(exceptionInfos.size());
        rootInfo.setExceptions(exceptionInfos);
        List<String> methodInfos = new ArrayList<>();
        recursionMethod(rootInfo, methodInfos);
        methodInfos.clear();
        return rootInfo;
    }

    public void recursionMethod(MethodInfo rootInfo, List<String> methodInfos) {
        List<MethodInfo> children = getChildren(rootInfo.getId());
        if (children != null && children.size() > 0) {
            if (!methodInfos.contains(rootInfo.getId())) {
                methodInfos.add(rootInfo.getId());
                rootInfo.setChildren(children);
                for (MethodInfo child : children) {
                    recursionMethod(child, methodInfos);
                }
            }

        }

    }

    @Override
    public boolean clearAll() {
      synchronized (this){
          methodNodes.clear();
          exceptions.clear();
          methodRelations.clear();
          exceptionRelations.clear();
          paramValueMetricMap.clear();
      }
        return true;
    }
}
