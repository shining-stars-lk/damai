package com.example.common;


import com.example.enums.MethodLevel;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.example.constant.ApiStatConstant.API_SPLIT;

public interface ApiStatCommon {
    public final static List<Class<?>> baseTypes = Arrays.asList(Integer.class, Double.class, Float.class, Long.class, String.class, Boolean.class, MultipartFile.class, List.class, Map.class);
    public final static List<Class<?>> excludedTypes = Arrays.asList(HttpServletRequest.class, HttpServletResponse.class,Model.class);

    default String getApi(MethodInvocation methodInvocation) {
        Class<?> targetClass = methodInvocation.getThis().getClass();
        String[] controllerClassApi = getControllerClassApi(targetClass);
        StringBuilder api = new StringBuilder("");
        if (controllerClassApi != null && controllerClassApi.length > 0) {
            api = new StringBuilder(controllerClassApi[0]);
        }
        String[] controllerMethodApi = getControllerMethodApi(methodInvocation.getMethod());
        if (controllerMethodApi == null || controllerMethodApi.length == 0) {
            return null;
        }
        if (controllerMethodApi[0].startsWith("/")) {
            api.append(controllerMethodApi[0]);
        } else {
            api.append(API_SPLIT + controllerMethodApi[0]);
        }
        return api.toString();
    }

    default String[] getControllerClassApi(Class<?> controllerClass) {
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            return requestMapping.value();
        }
        PostMapping postMapping = controllerClass.getAnnotation(PostMapping.class);
        if (postMapping != null) {
            return postMapping.value();
        }
        GetMapping getMapping = controllerClass.getAnnotation(GetMapping.class);
        if (getMapping != null) {
            return getMapping.value();
        }
        PutMapping putMapping = controllerClass.getAnnotation(PutMapping.class);
        if (putMapping != null) {
            return putMapping.value();
        }
        DeleteMapping deleteMapping = controllerClass.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) {
            return deleteMapping.value();
        }
        return null;
    }

    default String[] getControllerMethodApi(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            return requestMapping.value();
        }
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping != null) {
            return postMapping.value();
        }
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping != null) {
            return getMapping.value();
        }
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        if (putMapping != null) {
            return putMapping.value();
        }
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) {
            return deleteMapping.value();
        }
        return null;
    }

    MethodLevel getMethodLevel(MethodInvocation pjp);

}

