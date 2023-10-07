package com.example.common;


import com.example.enums.MethodLevel;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

import static com.example.constant.ApiStatConstant.API_SPLIT;
import static com.example.constant.ApiStatConstant.SPRING_APPLICATION_NAME;

public abstract class ApiStatCommon implements EnvironmentAware {
    
    protected Environment environment;  
    
    public String getApplicationName(){
        return environment.getProperty(SPRING_APPLICATION_NAME);
    }

    public String getApi(MethodInvocation methodInvocation) {
        Class<?> targetClass = methodInvocation.getThis().getClass();
        String[] controllerClassApi = getControllerClassApi(targetClass);
        StringBuilder api = new StringBuilder(getApplicationName());
        if (controllerClassApi != null && controllerClassApi.length > 0) {
            if (!controllerClassApi[0].startsWith(API_SPLIT)) {
                api.append(API_SPLIT);
            }
            api.append(controllerClassApi[0]);
        }
        String[] controllerMethodApi = getControllerMethodApi(methodInvocation.getMethod());
        if (controllerMethodApi == null || controllerMethodApi.length == 0) {
            return null;
        }
        if (!controllerMethodApi[0].startsWith(API_SPLIT)) {
            api.append(API_SPLIT);
        }
        api.append(controllerMethodApi[0]);
        return api.toString();
    }

    public String[] getControllerClassApi(Class<?> controllerClass) {
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

    public String[] getControllerMethodApi(Method method) {
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

    public abstract MethodLevel getMethodLevel(MethodInvocation pjp);
    
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}

