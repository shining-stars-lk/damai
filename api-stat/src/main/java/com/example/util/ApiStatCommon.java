package com.example.util;


import com.example.model.ApiStatExceptionNode;
import com.example.model.ApiStatInvokedInfo;
import com.example.model.ApiStatMethodNode;
import com.example.service.ApiStatMethodNodeService;
import lombok.Data;
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

@Data
public class ApiStatCommon {

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

    public static ApiStatMethodType getMethodType(MethodInvocation pjp) {
        Class<?> targetClass = pjp.getThis().getClass();
        if (targetClass.getAnnotation(Controller.class) != null || targetClass.getAnnotation(RestController.class) != null) {
            return ApiStatMethodType.Controller;
        } else if (targetClass.getAnnotation(Service.class) != null) {
            return ApiStatMethodType.Service;
        } else if (targetClass.getAnnotation(Repository.class) != null) {
            return ApiStatMethodType.Dao;
        }
        String className = pjp.getMethod().getDeclaringClass().getName().toLowerCase();
        if (className.contains("controller")) {
            return ApiStatMethodType.Controller;
        } else if (className.contains("service")) {
            return ApiStatMethodType.Service;
        } else if (className.contains("dao") || className.contains("mapper") || className.contains("com.sun.proxy.$Proxy")) {
            return ApiStatMethodType.Dao;
        } else {
            return ApiStatMethodType.Others;
        }
    }

    public static ApiStatMethodType getMethodType(String className) {
        className = className.toLowerCase();
        if (className.contains("controller")) {
            return ApiStatMethodType.Controller;
        } else if (className.contains("service")) {
            return ApiStatMethodType.Service;
        } else if (className.contains("dao") || className.contains("mapper") || className.contains("com.sun.proxy.$Proxy")) {
            return ApiStatMethodType.Dao;
        } else {
            return ApiStatMethodType.Others;
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

    public static ApiStatInvokedInfo getInvokedInfo(MethodInvocation invocation, ApiStatMethodNode parent, double runTime) {
        ApiStatMethodNode current = ApiStatMethodNodeService.getCurrentMethodNode(invocation, runTime);
        parent = checkControllerParent(parent, current);
        ApiStatInvokedInfo invokedInfo = new ApiStatInvokedInfo();
        invokedInfo.setCurrent(current);
        invokedInfo.setParent(parent);
        invokedInfo.setNames(invocation.getMethod().getParameters());
        invokedInfo.setValues(invocation.getArguments());
        return invokedInfo;
    }

    public static ApiStatInvokedInfo getInvokedInfoWithException(MethodInvocation invocation, ApiStatMethodNode parent, Exception e, double runTime) {
        ApiStatExceptionNode exception = new ApiStatExceptionNode();
        exception.setName(e.getClass().getSimpleName());
        exception.setClassName(e.getClass().getName());
        exception.setMessage(e.getMessage() + "");
        exception.setId(exception.getClassName() + "." + exception.getName());
        ApiStatMethodNode current = ApiStatMethodNodeService.getCurrentMethodNode(invocation, runTime);
        ApiStatInvokedInfo invokedInfo = new ApiStatInvokedInfo();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            if (stackTraceElement.getClassName().equals(current.getClassName())) {
                exception.setValue(stackTraceElement.getLineNumber());
                invokedInfo.setCurrent(current);
                parent = checkControllerParent(parent, current);
                invokedInfo.setParent(parent);
                invokedInfo.setApiStatExceptionNode(exception);
                invokedInfo.setNames(invocation.getMethod().getParameters());
                invokedInfo.setValues(invocation.getArguments());
                break;
            }
        }
        return invokedInfo;
    }

    private static ApiStatMethodNode checkControllerParent(ApiStatMethodNode parent, ApiStatMethodNode current) {
        if (current.getApiStatMethodType() == ApiStatMethodType.Controller) {
            parent = new ApiStatMethodNode();
            parent.setId("com.langpy.kotime.Controller.dispatch");
            parent.setClassName("Controller");
            parent.setMethodName("dispatch");
            parent.setName("Controller.dispatch");
            parent.setApiStatMethodType(ApiStatMethodType.Dispatcher);
        }
        return parent;
    }

}

