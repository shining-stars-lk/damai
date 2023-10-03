package com.example.util;


import com.example.enums.MethodType;
import lombok.extern.slf4j.Slf4j;
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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
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

}

