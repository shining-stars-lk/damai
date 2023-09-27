package com.example.data;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * zhangchang
 */
@Lazy
@Component("redis")
public class RedisBase implements GraphService {

    private static final String methodPre = Context.getConfig().getDataPrefix()+":KO_METHODS:";
    private static final String exceptionPre = Context.getConfig().getDataPrefix()+":KO_EXCEPTIONS:";
    private static final String methodRelationPre = Context.getConfig().getDataPrefix()+":KO_METHOD_RES:";
    private static final String exceptionRelationPre = Context.getConfig().getDataPrefix()+":KO_EXCEPTION_RES:";
    private static final String paramValueMetricMapPre = Context.getConfig().getDataPrefix()+":KO_PARAMETERS:";

    private static StringRedisTemplate redisTemplate;

    public RedisBase() {
        redisTemplate = Context.getStringRedisTemplate();

    }

    @Override
    public void addMethodNode(MethodNode methodNode) {
        if (null == methodNode) {
            return;
        }
        String key = methodPre + methodNode.getId();
        if (!redisTemplate.hasKey(key)) {
            insert(key, methodNode);
        } else {
            if (methodNode.getMethodType() == MethodType.Controller && !Common.isEmpty(methodNode.getRouteName())) {
                MethodNode controller = query(key, MethodNode.class);
                if (controller==null) {
                    return;
                }
                controller.setRouteName(methodNode.getRouteName());
                insert(key, controller);
            }
        }
    }

    @Override
    public void addParamAnalyse(String methodId, Parameter[] names, Object[] values, double v) {
        String paramsKey = Common.getPramsStr(names, values);
        String key = paramValueMetricMapPre + methodId;
        if (redisTemplate.hasKey(key)) {
            Map<String, JSONObject> paramMetricMap = query(key, Map.class);
            if (paramMetricMap==null) {
                return;
            }
            if (paramMetricMap.containsKey(paramsKey)) {
                if (Math.random()<Context.getConfig().getDiscardRate()) {
                    return;
                }
                ParamMetric paramMetric = paramMetricMap.get(paramsKey).toJavaObject(ParamMetric.class);
                BigDecimal bg = BigDecimal.valueOf((paramMetric.getAvgRunTime() + v) / 2.0);
                double avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                paramMetric.setAvgRunTime(avg);
                if (v > paramMetric.getMaxRunTime()) {
                    paramMetric.setMaxRunTime(v);
                }
                if (v < paramMetric.getMinRunTime()) {
                    paramMetric.setMinRunTime(v);
                }
                paramMetricMap.put(paramsKey, (JSONObject) JSONObject.toJSON(paramMetric));
                insert(key, paramMetricMap);
            }else {
                ParamMetric paramMetric = new ParamMetric();
                paramMetric.setMaxRunTime(v);
                paramMetric.setAvgRunTime(v);
                paramMetric.setMaxRunTime(v);
                paramMetricMap.put(paramsKey, (JSONObject)JSONObject.toJSON(paramMetric));
                insert(key, paramMetricMap);
            }
        } else {
            ParamMetric paramMetric = new ParamMetric();
            paramMetric.setMaxRunTime(v);
            paramMetric.setAvgRunTime(v);
            paramMetric.setMaxRunTime(v);
            Map<String, ParamMetric> paramMetricMap = new HashMap<>();
            paramMetricMap.put(paramsKey, paramMetric);
            insert(key, paramMetricMap);
        }
    }


    @Override
    public MethodRelation addMethodRelation(MethodNode sourceMethodNode, MethodNode targetMethodNode) {
        if (null == sourceMethodNode || null == targetMethodNode) {
            return null;
        }
        if (sourceMethodNode.getId().equals(targetMethodNode.getId())) {
            return null;
        }
        if (targetMethodNode.getMethodType()==MethodType.Controller && !"Controller.dispatch".equals(sourceMethodNode.getName())) {
            return null;
        }
        if (redisTemplate.hasKey(methodRelationPre + targetMethodNode.getId() + sourceMethodNode.getId())) {
            return null;
        }
        MethodRelation methodRelation = new MethodRelation();
        methodRelation.setSourceId(sourceMethodNode.getId());
        methodRelation.setTargetId(targetMethodNode.getId());
        methodRelation.setId(sourceMethodNode.getId() + targetMethodNode.getId());
        methodRelation.setAvgRunTime(targetMethodNode.getValue());
        methodRelation.setMaxRunTime(targetMethodNode.getValue());
        methodRelation.setMinRunTime(targetMethodNode.getValue());
        String key = methodRelationPre + methodRelation.getId();
        MethodRelation old = query(key, MethodRelation.class);
        if (null == old) {
            insert(key, methodRelation);
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
            insert(key, old);
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
        String key = exceptionRelationPre + exceptionRelation.getId();
        ExceptionRelation old = query(key, ExceptionRelation.class);
        if (null == old) {
            insert(key, exceptionRelation);
            return exceptionRelation;
        } else {
            return old;
        }
    }

    @Override
    public void addExceptionNode(ExceptionNode exceptionNode) {
        String key = exceptionPre + exceptionNode.getId();
        if (!redisTemplate.hasKey(key)) {
            insert(key, exceptionNode);
        }
    }

    @Override
    public List<ExceptionInfo> getExceptions(String methodId) {
        List<ExceptionInfo> exceptionInfos = new ArrayList<>();
        List<ExceptionRelation> searchs = searchList(exceptionRelationPre, ExceptionRelation.class);
        List<ExceptionRelation> relations = searchs.stream().filter(exceptionRelation -> exceptionRelation.getSourceId().equals(methodId)).collect(toList());
        for (ExceptionRelation relation : relations) {
            String exceptionId = relation.getTargetId();
            ExceptionNode exceptionNode = query(exceptionPre + exceptionId, ExceptionNode.class);
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
    public List<ExceptionNode> getExceptions() {
        List<ExceptionNode> exceptionInfos = new ArrayList<>();

        List<ExceptionNode> exceptionNodes = searchList(exceptionPre, ExceptionNode.class);
        List<ExceptionRelation> exceptionRelations = searchList(exceptionRelationPre, ExceptionRelation.class);
        for (ExceptionNode exceptionNode : exceptionNodes) {
            List<ExceptionRelation> relations = exceptionRelations.stream().filter(relation -> relation.getTargetId().equals(exceptionNode.getId())).collect(toList());
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
        List<MethodNode> smethodNodes = searchList(methodPre, MethodNode.class);

        for (MethodNode methodNode : smethodNodes) {
            if (MethodType.Controller == methodNode.getMethodType()) {
                String id = methodNode.getId();
                List<MethodRelation> smethodRelations = searchList(methodRelationPre, MethodRelation.class);

                Optional<MethodRelation> relations = smethodRelations.stream().filter(methodRelation -> methodRelation.getTargetId().equals(id)).findFirst();
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
        String key = paramValueMetricMapPre + methodId;
        Map<String, ParamMetric> res = new HashMap<>();
        Map<String, JSONObject> paramMetric = query(key, Map.class);
        for (String name : paramMetric.keySet()) {
            res.put(name, paramMetric.get(name).toJavaObject(ParamMetric.class));
        }
        return res;
    }

    @Override
    public List<MethodInfo> searchMethods(String question) {
        List<MethodInfo> methodInfos = new ArrayList<>();
        List<MethodNode> smethodNodes = searchList(methodPre, MethodNode.class);
        List<MethodRelation> methodRelationList = searchList(methodRelationPre, MethodRelation.class);
        for (MethodNode methodNode : smethodNodes) {
            if (methodNode.getName().toLowerCase().contains(question.toLowerCase())) {
                String id = methodNode.getId();
                Optional<MethodRelation> relations = methodRelationList.stream().filter(methodRelation -> methodRelation.getTargetId().equals(id)).findFirst();
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
        List<MethodNode> smethodNodes = searchList(methodPre, MethodNode.class);
        for (MethodNode methodNode : smethodNodes) {
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
    public List<ExceptionInfo> getExceptionInfos(String exceptionId,String message) {
        List<ExceptionInfo> exceptionInfos = new ArrayList<>();
        List<ExceptionRelation> sexceptionRelations = searchList(exceptionRelationPre, ExceptionRelation.class);
        for (ExceptionRelation relation : sexceptionRelations) {
            if (relation.getTargetId().equals(exceptionId) && relation.getMessage().equals(message)) {
                String sourceMethodId = relation.getSourceId();
                MethodNode methodNode = query(methodPre + sourceMethodId, MethodNode.class);
                if (methodNode==null) {
                    continue;
                }
                ExceptionNode exceptionNode = query(exceptionPre + exceptionId, ExceptionNode.class);
                if (exceptionNode==null) {
                    continue;
                }
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
    public List<MethodInfo> getChildren(String methodId) {
        List<MethodInfo> methodInfos = new ArrayList<>();
        List<MethodRelation> methodRelationList = searchList(methodRelationPre, MethodRelation.class);
        for (MethodRelation methodRelation : methodRelationList) {
            if (methodRelation.getSourceId().equals(methodId)) {
                String targetMethodId = methodRelation.getTargetId();
                MethodNode methodNode = query(methodPre + targetMethodId, MethodNode.class);
                if (methodNode == null) {
                    continue;
                }
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
        MethodNode methodNode = query(methodPre + methodId, MethodNode.class);
        if (null==methodNode) {
            return rootInfo;
        }
        rootInfo.setId(methodNode.getId());
        rootInfo.setName(methodNode.getName());
        rootInfo.setClassName(methodNode.getClassName());
        rootInfo.setMethodName(methodNode.getMethodName());
        rootInfo.setMethodType(methodNode.getMethodType());
        rootInfo.setRouteName(methodNode.getRouteName());
        List<MethodRelation> methodRelationList = searchList(methodRelationPre, MethodRelation.class);

        MethodRelation methodRelation = methodRelationList.stream().filter(relation -> relation.getTargetId().equals(methodId)).findFirst().get();
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


    private void insert(String key, Object o) {
        redisTemplate.opsForValue().set(key, toJson(o));
    }

    private <T> T query(String key, Class<T> c) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            return null;
        }
        return toObject(o.toString(), c);
    }

    private <T> List<T> searchList(String pre, Class<T> c) {
        List<T> res = new ArrayList<>();
        Set<String> keys = redisTemplate.keys(pre + "*");
        if (keys == null || keys.size() == 0) {
            return res;
        }
        List<String> objects = redisTemplate.opsForValue().multiGet(keys);
        if (objects == null || objects.size() == 0) {
            return res;
        }
        for (Object object : objects) {
            res.add(toObject(object.toString(), c));
        }
        return res;
    }

    private String toJson(Object o) {
        return JSON.toJSONString(o);
    }

    private <T> T toObject(String str, Class<T> c) {
        return JSONObject.parseObject(str, c);
    }

    @Override
    public boolean clearAll() {
      synchronized (this){
          List<String> keys = Arrays.asList(methodPre,methodRelationPre,exceptionPre,exceptionRelationPre,paramValueMetricMapPre);
          for (String key : keys) {
              Set<String> deleteKeys = redisTemplate.keys(key + "*");
              if (deleteKeys==null || deleteKeys.size()==0) {
                  continue;
              }
              redisTemplate.delete(deleteKeys);
          }
      }
        return true;
    }
}
