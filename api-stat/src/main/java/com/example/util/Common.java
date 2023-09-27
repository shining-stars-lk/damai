package com.example.util;


import com.example.model.ExceptionNode;
import com.example.model.InvokedInfo;
import com.example.model.MethodNode;
import com.example.service.MethodNodeService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * zhangchang
 */
public class Common {
    private static Logger log = Logger.getLogger(Common.class.toString());

    private final static List<Class<?>> baseTypes = Arrays.asList(Integer.class, Double.class, Float.class, Long.class, String.class, Boolean.class, MultipartFile.class, List.class, Map.class);
    private final static List<Class<?>> excludedTypes = Arrays.asList(HttpServletRequest.class, HttpServletResponse.class,Model.class);

    public static String getRoute(MethodInvocation pjp) {
        Class<?> targetClass = pjp.getThis().getClass();
        String[] classRoute = getRouteValue(targetClass);
        StringBuilder routes = new StringBuilder("");
        if (classRoute != null && classRoute.length > 0) {
            routes = new StringBuilder(classRoute[0]);
        }
        String[] methodRoute = getRouteValue(pjp.getMethod());
        if (methodRoute == null || methodRoute.length == 0) {
            return null;
        }
        if (methodRoute[0].startsWith("/")) {
            routes.append(methodRoute[0]);
        } else {
            routes.append("/" + methodRoute[0]);
        }
        return routes.toString();
    }

    private static String[] getRouteValue(Class<?> targetClass) {
        RequestMapping methodAnnotationRequest = targetClass.getAnnotation(RequestMapping.class);
        if (methodAnnotationRequest != null) {
            return methodAnnotationRequest.value();
        }
        PostMapping methodAnnotationPost = targetClass.getAnnotation(PostMapping.class);
        if (methodAnnotationPost != null) {
            return methodAnnotationPost.value();
        }
        GetMapping methodAnnotationGet = targetClass.getAnnotation(GetMapping.class);
        if (methodAnnotationGet != null) {
            return methodAnnotationGet.value();
        }
        PutMapping methodAnnotationPut = targetClass.getAnnotation(PutMapping.class);
        if (methodAnnotationPut != null) {
            return methodAnnotationPut.value();
        }
        DeleteMapping methodAnnotationDelete = targetClass.getAnnotation(DeleteMapping.class);
        if (methodAnnotationDelete != null) {
            return methodAnnotationDelete.value();
        }
        return null;
    }

    private static String[] getRouteValue(Method method) {
        RequestMapping methodAnnotationRequest = method.getAnnotation(RequestMapping.class);
        if (methodAnnotationRequest != null) {
            return methodAnnotationRequest.value();
        }
        PostMapping methodAnnotationPost = method.getAnnotation(PostMapping.class);
        if (methodAnnotationPost != null) {
            return methodAnnotationPost.value();
        }
        GetMapping methodAnnotationGet = method.getAnnotation(GetMapping.class);
        if (methodAnnotationGet != null) {
            return methodAnnotationGet.value();
        }
        PutMapping methodAnnotationPut = method.getAnnotation(PutMapping.class);
        if (methodAnnotationPut != null) {
            return methodAnnotationPut.value();
        }
        DeleteMapping methodAnnotationDelete = method.getAnnotation(DeleteMapping.class);
        if (methodAnnotationDelete != null) {
            return methodAnnotationDelete.value();
        }
        return null;
    }

    public static MethodType getMethodType(MethodInvocation pjp) {
        Class<?> targetClass = pjp.getThis().getClass();
        if (targetClass.getAnnotation(Controller.class) != null || targetClass.getAnnotation(RestController.class) != null) {
            return MethodType.Controller;
        } else if (targetClass.getAnnotation(Service.class) != null) {
            return MethodType.Service;
        } else if (targetClass.getAnnotation(Repository.class) != null) {
            return MethodType.Dao;
        }
        String className = pjp.getMethod().getDeclaringClass().getName().toLowerCase();
        if (className.contains("controller")) {
            return MethodType.Controller;
        } else if (className.contains("service")) {
            return MethodType.Service;
        } else if (className.contains("dao") || className.contains("mapper") || className.contains("com.sun.proxy.$Proxy")) {
            return MethodType.Dao;
        } else {
            return MethodType.Others;
        }
    }

    public static MethodType getMethodType(String className) {
        className = className.toLowerCase();
        if (className.contains("controller")) {
            return MethodType.Controller;
        } else if (className.contains("service")) {
            return MethodType.Service;
        } else if (className.contains("dao") || className.contains("mapper") || className.contains("com.sun.proxy.$Proxy")) {
            return MethodType.Dao;
        } else {
            return MethodType.Others;
        }
    }

    public static void showLog(MethodNode current) {
        showLog(current.getId(), current.getValue());
    }

    public static void showLog(String method, double value) {
        if ("chinese".equals(Context.getConfig().getLanguage())) {
            log.info("调用方法=" + method + "()，耗时=" + value + "毫秒");
        } else if ("english".equals(Context.getConfig().getLanguage())) {
            log.info("method=" + method + "(),runTime=" + value + "ms");
        }
    }

    public static String getPramsStr(Parameter[] names, Object[] values) {
        String paramsKey = "-";
        if (names == null) {
            return paramsKey;
        }
        List<String> params = new ArrayList<>();
        int namesLen = names.length;
        for (int i = 0; i < namesLen; i++) {
            Object valuesI = values[i];
            if (isEmpty(valuesI)) {
                continue;
            }
            Class<?> type = names[i].getType();
            if (baseTypes.contains(type)) {
                params.add(names[i].getName());
            } else {
                if (excludedTypes.contains(type)) {
                    continue;
                }
                Field[] declaredFields = valuesI.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }
                    try {
                        field.setAccessible(true);
                        Object value = field.get(valuesI);
                        if (isEmpty(value)) {
                            continue;
                        }
                        params.add(field.getName());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }
        if (params.size() > 0) {
            paramsKey = String.join("-", params);
        }
        return paramsKey;
    }

    public static boolean isEmpty(Object value) {
        return value == null || "".equals(value) || ((value instanceof String) && ((String) value).trim().length() == 0);
    }

    public static InvokedInfo getInvokedInfo(MethodInvocation invocation, MethodNode parent, double runTime) {
        MethodNode current = MethodNodeService.getCurrentMethodNode(invocation, runTime);
        parent = checkControllerParent(parent, current);
        InvokedInfo invokedInfo = new InvokedInfo();
        invokedInfo.setCurrent(current);
        invokedInfo.setParent(parent);
        invokedInfo.setNames(invocation.getMethod().getParameters());
        invokedInfo.setValues(invocation.getArguments());
        return invokedInfo;
    }

    public static InvokedInfo getInvokedInfoWithException(MethodInvocation invocation, MethodNode parent, Exception e, double runTime) {
        ExceptionNode exception = new ExceptionNode();
        exception.setName(e.getClass().getSimpleName());
        exception.setClassName(e.getClass().getName());
        exception.setMessage(e.getMessage() + "");
        exception.setId(exception.getClassName() + "." + exception.getName());
        MethodNode current = MethodNodeService.getCurrentMethodNode(invocation, runTime);
        InvokedInfo invokedInfo = new InvokedInfo();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            if (stackTraceElement.getClassName().equals(current.getClassName())) {
                exception.setValue(stackTraceElement.getLineNumber());
                invokedInfo.setCurrent(current);
                parent = checkControllerParent(parent, current);
                invokedInfo.setParent(parent);
                invokedInfo.setException(exception);
                invokedInfo.setNames(invocation.getMethod().getParameters());
                invokedInfo.setValues(invocation.getArguments());
                break;
            }
        }
        return invokedInfo;
    }

    private static MethodNode checkControllerParent(MethodNode parent, MethodNode current) {
        if (current.getMethodType() == MethodType.Controller) {
            parent = new MethodNode();
            parent.setId("com.langpy.kotime.Controller.dispatch");
            parent.setClassName("Controller");
            parent.setMethodName("dispatch");
            parent.setName("Controller.dispatch");
            parent.setMethodType(MethodType.Dispatcher);
        }
        return parent;
    }

}

