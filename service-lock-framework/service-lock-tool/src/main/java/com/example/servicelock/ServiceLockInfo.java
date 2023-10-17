package com.example.servicelock;

import com.example.core.StringUtil;
import com.example.core.BaseInfo;
import org.aspectj.lang.JoinPoint;

import java.util.ArrayList;
import java.util.List;

import static com.example.core.Constants.SEPARATOR;

/**
 * @program: cook-frame
 * @description: 锁业务名和标识进行组装并获取类
 * @author: 星哥
 * @create: 2023-05-28
 **/
public class ServiceLockInfo extends BaseInfo {

    private static final String LOCK_NAME_PREFIX = "LOCK";

    private static final String LOCK_DISTRIBUTE_ID_NAME_PREFIX = "LOCK_DISTRIBUTE_ID";

    public String getLockName(JoinPoint joinPoint,String name,String[] keys){
        return LOCK_NAME_PREFIX + SEPARATOR + name + getRelKey(joinPoint, keys);
    }

    public String simpleGetLockName(String name,String[] keys){
        List<String> definitionKeyList = new ArrayList<>();
        for (String key : keys) {
            if (StringUtil.isNotEmpty(key)) {
                definitionKeyList.add(key);
            }
        }
        return LOCK_DISTRIBUTE_ID_NAME_PREFIX + SEPARATOR + name + SEPARATOR + String.join(SEPARATOR, definitionKeyList);
    }
}
