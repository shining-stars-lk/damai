package com.damai.lockinfo;


import com.damai.core.SpringUtil;
import com.damai.parser.ExtParameterNameDiscoverer;
import com.damai.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.damai.core.Constants.SEPARATOR;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 锁信息抽象
 * @author: 阿星不是程序员
 **/
@Slf4j
public abstract class AbstractLockInfoHandle implements LockInfoHandle {
    
    private static final String LOCK_DISTRIBUTE_ID_NAME_PREFIX = "LOCK_DISTRIBUTE_ID";

    private final ParameterNameDiscoverer nameDiscoverer = new ExtParameterNameDiscoverer();

    private final ExpressionParser parser = new SpelExpressionParser();
    
    /**
     * 锁信息前缀
     * @return 具体前缀
     * */
    protected abstract String getLockPrefixName();
    @Override
    public String getLockName(JoinPoint joinPoint,String name,String[] keys){
        return SpringUtil.getPrefixDistinctionName() + "-" + getLockPrefixName() + SEPARATOR + name + getRelKey(joinPoint, keys);
    }
    @Override
    public String simpleGetLockName(String name,String[] keys){
        List<String> definitionKeyList = new ArrayList<>();
        for (String key : keys) {
            if (StringUtil.isNotEmpty(key)) {
                definitionKeyList.add(key);
            }
        }
        return SpringUtil.getPrefixDistinctionName() + "-" + 
                LOCK_DISTRIBUTE_ID_NAME_PREFIX + SEPARATOR + name + SEPARATOR + String.join(SEPARATOR, definitionKeyList);
    }

    /**
     * 获取自定义键
     * */
    private String getRelKey(JoinPoint joinPoint, String[] keys){
        Method method = getMethod(joinPoint);
        List<String> definitionKeys = getSpElKey(keys, method, joinPoint.getArgs());
        return SEPARATOR + String.join(SEPARATOR, definitionKeys);
    }
    
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(),
                        method.getParameterTypes());
            } catch (Exception e) {
                log.error("get method error ",e);
            }
        }
        return method;
    }

    private List<String> getSpElKey(String[] definitionKeys, Method method, Object[] parameterValues) {
        List<String> definitionKeyList = new ArrayList<>();
        for (String definitionKey : definitionKeys) {
            if (!ObjectUtils.isEmpty(definitionKey)) {
                EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, nameDiscoverer);
                Object objKey = parser.parseExpression(definitionKey).getValue(context);
                definitionKeyList.add(ObjectUtils.nullSafeToString(objKey));
            }
        }
        return definitionKeyList;
    }

}
