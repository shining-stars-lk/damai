package com.example.common.impl;

import com.example.common.ApiStatCommon;
import com.example.enums.MethodLevel;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.example.constant.ApiStatConstant.*;

public class ApiStatSingleServiceCommon implements ApiStatCommon {
    @Override
    public MethodLevel getMethodLevel(MethodInvocation pjp) {
        Class<?> targetClass = pjp.getThis().getClass();
        if (targetClass.getAnnotation(Controller.class) != null || targetClass.getAnnotation(RestController.class) != null) {
            return MethodLevel.Controller;
        } else if (targetClass.getAnnotation(Service.class) != null) {
            return MethodLevel.Service;
        } else if (targetClass.getAnnotation(Repository.class) != null) {
            return MethodLevel.Dao;
        }
        String className = pjp.getMethod().getDeclaringClass().getName().toLowerCase();
        if (className.contains(CONTROLLER)) {
            return MethodLevel.Controller;
        } else if (className.contains(SERVICE)) {
            return MethodLevel.Service;
        } else if (className.contains(DAO) || className.contains(MAPPER)) {
            return MethodLevel.Dao;
        } else {
            return MethodLevel.Others;
        }
    }
}
