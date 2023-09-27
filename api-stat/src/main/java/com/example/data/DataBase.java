package com.example.data;


import com.example.constant.KoSqlConstant;
import com.example.model.ExceptionInfo;
import com.example.model.ExceptionNode;
import com.example.model.ExceptionRelation;
import com.example.model.MethodInfo;
import com.example.model.MethodNode;
import com.example.model.MethodRelation;
import com.example.model.ParamAna;
import com.example.model.ParamMetric;
import com.example.model.SystemStatistic;
import com.example.service.GraphService;
import com.example.util.Common;
import com.example.util.Context;
import com.example.util.DataBaseException;
import com.example.util.DataBaseUtil;
import com.example.util.MethodType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

/**
 * zhangchang
 */
@Component("database")
@Lazy
public class DataBase implements GraphService {
    private static Logger log = Logger.getLogger(DataBase.class.toString());

    private Connection writeConnection;

    public DataBase() {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    try {
                        if (null != writeConnection) {
                            writeConnection.close();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } finally {
                        log.info("kotime=>closed database connections...");
                    }
                })
        );
        initConnection();
    }

    public void initConnection() {
        getWriteConnection();
    }

    public Connection getWriteConnection() {
        try {
            if (null == writeConnection || writeConnection.isClosed()) {
                DataSource dataSource = Context.getDataSource();
                if (null == dataSource) {
                    if ("database".equals(Context.getConfig().getSaver())) {
                        throw new DataBaseException("`ko-time.saver=database` needs a DataSource for MySQl or Oracle, or you can use `ko-time.saver=memory` to store data!");
                    }
                } else {
                    writeConnection = dataSource.getConnection();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return writeConnection;
    }

    @Override
    public void addMethodNode(MethodNode methodNode) {
        if (null == methodNode) {
            return;
        }
        boolean existsById = DataBaseUtil.existsById(getWriteConnection(), KoSqlConstant.queryMethodExist, methodNode.getId());
        if (!existsById) {
            Object[] params = new Object[]{
                    methodNode.getId(),
                    methodNode.getName(),
                    methodNode.getClassName(),
                    methodNode.getMethodName(),
                    methodNode.getRouteName(),
                    methodNode.getMethodType().name()
            };
            DataBaseUtil.insert(getWriteConnection(), KoSqlConstant.addMethod, params);
        } else {
            if (methodNode.getMethodType() == MethodType.Controller && !Common.isEmpty(methodNode.getRouteName())) {
                Object[] params = new Object[]{
                        methodNode.getName(),
                        methodNode.getClassName(),
                        methodNode.getMethodName(),
                        methodNode.getRouteName(),
                        methodNode.getMethodType().name(),
                        methodNode.getId(),
                };
                DataBaseUtil.update(getWriteConnection(), KoSqlConstant.updateMethod, params);
            }
        }
    }


    @Override
    public void addExceptionNode(ExceptionNode exceptionNode) {
        if (exceptionNode == null) {
            return;
        }

        Object[] params = new Object[]{
                exceptionNode.getId(),
                exceptionNode.getName(),
                exceptionNode.getClassName()
        };
        DataBaseUtil.insert(getWriteConnection(), KoSqlConstant.addException, params);
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
        try {
            List<Map<String, Object>> query = DataBaseUtil.query(getWriteConnection(), KoSqlConstant.queryMethodRe, new Object[]{sourceMethodNode.getId() + targetMethodNode.getId()});
            if (query.size() > 0) {
                if (Math.random() < Context.getConfig().getDiscardRate()) {
                    return null;
                }
                Map<String, Object> old = query.get(0);
                double oldAvg = (double) old.get("avg_run_time");
                double oldMax = (double) old.get("max_run_time");
                double oldMin = (double) old.get("min_run_time");
                BigDecimal bg = BigDecimal.valueOf((targetMethodNode.getValue() + oldAvg) / 2.0);
                double avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                double max = targetMethodNode.getValue() > oldMax ? targetMethodNode.getValue() : oldMax;
                double min = targetMethodNode.getValue() < oldMin ? targetMethodNode.getValue() : oldMin;
                Object[] params = new Object[]{
                        sourceMethodNode.getId(),
                        targetMethodNode.getId(),
                        avg,
                        max,
                        min,
                        sourceMethodNode.getId() + targetMethodNode.getId()
                };
                DataBaseUtil.update(getWriteConnection(), KoSqlConstant.updateMethodRe, params);
                return null;
            } else {
                Object[] params = new Object[]{
                        sourceMethodNode.getId() + targetMethodNode.getId(),
                        sourceMethodNode.getId(),
                        targetMethodNode.getId(),
                        targetMethodNode.getValue(),
                        targetMethodNode.getValue(),
                        targetMethodNode.getValue()
                };
                DataBaseUtil.insert(getWriteConnection(), KoSqlConstant.addMethodRe, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ExceptionRelation addExceptionRelation(MethodNode sourceMethodNode, ExceptionNode exceptionNode) {
        String id = sourceMethodNode.getId()+exceptionNode.getId()+exceptionNode.getMessage()+exceptionNode.getValue();
        boolean existsById = DataBaseUtil.existsById(getWriteConnection(), KoSqlConstant.queryExceptionReExist, id);
        if (!existsById) {
            Object[] params = new Object[]{
                    id,
                    sourceMethodNode.getId(),
                    exceptionNode.getId(),
                    exceptionNode.getValue(),
                    exceptionNode.getMessage()
            };
            DataBaseUtil.insert(getWriteConnection(), KoSqlConstant.addExceptionRe, params);
        }
        return null;
    }

    @Override
    public void addParamAnalyse(String methodId, Parameter[] names, Object[] values, double v) {
        String paramsKey = Common.getPramsStr(names, values);
        List<Map<String, Object>> query = DataBaseUtil.query(getWriteConnection(), KoSqlConstant.queryParamsAna, new Object[]{methodId, paramsKey});
        if (query.size() == 0) {
            Object[] params = new Object[]{
                    methodId,
                    paramsKey,
                    v,
                    v,
                    v
            };
            DataBaseUtil.insert(getWriteConnection(), KoSqlConstant.addParamsAna, params);
        } else {
            if (Math.random() < Context.getConfig().getDiscardRate()) {
                return;
            }
            Map<String, Object> old = query.get(0);
            double oldAvg = (double) old.get("avg_run_time");
            double oldMax = (double) old.get("max_run_time");
            double oldMin = (double) old.get("min_run_time");
            BigDecimal bg = BigDecimal.valueOf((v + oldAvg) / 2.0);
            double avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double max = v > oldMax ? v : oldMax;
            double min = v < oldMin ? v : oldMin;
            Object[] params = new Object[]{
                    avg,
                    max,
                    min,
                    methodId,
                    paramsKey
            };
            DataBaseUtil.update(getWriteConnection(), KoSqlConstant.updateParamsAna, params);
        }
    }


    @Override
    public MethodInfo getTree(String methodId) {
        MethodInfo rootInfo = new MethodInfo();
        List<MethodNode> methodNodes = DataBaseUtil.query(KoSqlConstant.queryMethod, new Object[]{methodId}, MethodNode.class);
        if (methodNodes.size() == 0) {
            return rootInfo;
        }

        MethodNode methodNode = methodNodes.get(0);
        rootInfo.setId(methodNode.getId());
        rootInfo.setName(methodNode.getName());
        rootInfo.setClassName(methodNode.getClassName());
        rootInfo.setMethodName(methodNode.getMethodName());
        rootInfo.setMethodType(methodNode.getMethodType());
        rootInfo.setRouteName(methodNode.getRouteName());

        List<MethodRelation> relations = DataBaseUtil.query(KoSqlConstant.queryMethodReByTarget, new Object[]{methodId}, MethodRelation.class);
        if (relations.size() == 0) {
            return rootInfo;
        }
        MethodRelation methodRelation = relations.get(0);

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
    public Map<String, ParamMetric> getMethodParamGraph(String methodId) {
        Map<String, ParamMetric> paramMetricMap = new HashMap<>();
        List<ParamAna> paramAnas = DataBaseUtil.query(KoSqlConstant.queryParamsAnaBySource, new Object[]{methodId}, ParamAna.class);

        if (paramAnas.size() == 0) {
            return paramMetricMap;
        }
        for (ParamAna paramAna : paramAnas) {
            if (!paramMetricMap.containsKey(paramAna.getSourceId())) {
                ParamMetric paramMetric = new ParamMetric();
                paramMetric.setAvgRunTime(paramAna.getAvgRunTime());
                paramMetric.setMaxRunTime(paramAna.getMaxRunTime());
                paramMetric.setMinRunTime(paramAna.getMinRunTime());
                paramMetricMap.put(paramAna.getParams(), paramMetric);
            }
        }
        return paramMetricMap;
    }

    @Override
    public SystemStatistic getRunStatistic() {
        SystemStatistic systemStatistic = new SystemStatistic();
        List<MethodInfo> controllerApis = getControllers();
        if (null == controllerApis || controllerApis.size() == 0) {
            return systemStatistic;
        }

        int delayNum = 0;
        int totalNum = controllerApis.size();
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        double avg = controllerApis.get(0).getAvgRunTime();
        for (MethodInfo controllerApi : controllerApis) {
            double avgRunTime = controllerApi.getAvgRunTime();
            if (avgRunTime >= Context.getConfig().getThreshold()) {
                delayNum++;
            }
            if (avgRunTime > max) {
                max = avgRunTime;
            }
            if (avgRunTime < min) {
                min = avgRunTime;
            }
            avg = (avgRunTime + avg) / 2;
        }
        systemStatistic.setDelayNum(delayNum);
        systemStatistic.setNormalNum(totalNum - delayNum);
        systemStatistic.setTotalNum(totalNum);

        BigDecimal bg = BigDecimal.valueOf(avg);
        avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        systemStatistic.setMaxRunTime(max);
        systemStatistic.setMinRunTime(min);
        systemStatistic.setAvgRunTime(avg);
        return systemStatistic;
    }

    @Override
    public List<MethodInfo> getControllers() {
        List<MethodInfo> methodInfos = DataBaseUtil.query( KoSqlConstant.queryControllers, null, MethodInfo.class);
        return methodInfos;
    }

    @Override
    public List<String> getCondidates(String question) {
        List<MethodNode> methodNodes = DataBaseUtil.query(KoSqlConstant.queryMethodLikeName, new Object[]{"%" + question + "%"}, MethodNode.class);
        List<String> methodInfos = new ArrayList<>();
        if (methodNodes.size() > 0) {
            methodInfos = methodNodes.stream().map(MethodNode::getName).collect(toList());
        }
        return methodInfos;
    }

    @Override
    public List<MethodInfo> searchMethods(String question) {
        List<MethodInfo> methodInfos = DataBaseUtil.query( KoSqlConstant.searchMethodsByName, new Object[]{"%" + question + "%"}, MethodInfo.class);
        return methodInfos;
    }

    @Override
    public List<MethodInfo> getChildren(String methodId) {
        List<MethodInfo> methodInfosResult = new ArrayList<>();
        List<MethodInfo> methodInfos = DataBaseUtil.query(KoSqlConstant.queryChildrenByParent, new Object[]{methodId}, MethodInfo.class);
        for (MethodInfo methodInfo : methodInfos) {
            List<ExceptionInfo> exceptionInfos = getExceptions(methodInfo.getId());
            methodInfo.setExceptionNum(exceptionInfos.size());
            methodInfo.setExceptions(exceptionInfos);
            if (!methodInfosResult.contains(methodInfo)) {
                methodInfosResult.add(methodInfo);
            }
        }
        return methodInfosResult;
    }

    @Override
    public List<ExceptionInfo> getExceptionInfos(String exceptionId, String message) {
        List<ExceptionRelation> relations = DataBaseUtil.query(KoSqlConstant.queryExceptionReByTargetAndMessage, new Object[]{exceptionId, message}, ExceptionRelation.class);
        List<ExceptionInfo> exceptionInfos = new ArrayList<>();
        for (ExceptionRelation relation : relations) {
            String sourceMethodId = relation.getSourceId();
            List<MethodNode> methodNodes = DataBaseUtil.query(KoSqlConstant.queryMethod, new Object[]{sourceMethodId}, MethodNode.class);
            if (methodNodes.size() == 0) {
                continue;
            }
            MethodNode methodNode = methodNodes.get(0);

            List<ExceptionNode> exceptions = DataBaseUtil.query( KoSqlConstant.queryException, new Object[]{exceptionId}, ExceptionNode.class);
            if (exceptions.size() == 0) {
                continue;
            }
            ExceptionNode exceptionNode = exceptions.get(0);
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
        return exceptionInfos;
    }

    @Override
    public List<ExceptionInfo> getExceptions(String methodId) {
        List<ExceptionInfo> exceptionInfos = new ArrayList<>();
        List<ExceptionRelation> relations = DataBaseUtil.query(KoSqlConstant.queryExceptionReBySource, new Object[]{methodId}, ExceptionRelation.class);
        for (ExceptionRelation relation : relations) {
            String exceptionId = relation.getTargetId();
            List<ExceptionNode> exceptionNodes = DataBaseUtil.query(KoSqlConstant.queryException, new Object[]{exceptionId}, ExceptionNode.class);
            if (exceptionNodes.size() == 0) {
                continue;
            }
            ExceptionNode exceptionNode = exceptionNodes.get(0);
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
        List<ExceptionNode> exceptionNodes = DataBaseUtil.query(KoSqlConstant.queryExceptions, null, ExceptionNode.class);
        return exceptionNodes;
    }

    /**
     * delete all data
     *
     * @return
     */
    @Override
    public boolean clearAll() {
        synchronized (this) {
            DataBaseUtil.truncateByTable(getWriteConnection(), "ko_method_node");
            DataBaseUtil.truncateByTable(getWriteConnection(), "ko_method_relation");
            DataBaseUtil.truncateByTable(getWriteConnection(), "ko_exception_node");
            DataBaseUtil.truncateByTable(getWriteConnection(), "ko_exception_relation");
            DataBaseUtil.truncateByTable(getWriteConnection(), "ko_param_ana");
        }

        return true;
    }
}
