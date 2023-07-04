package com.tool.core;

import java.io.Serializable;

/**
 * @program: redis-tool
 * @description: 常量类
 * @author: k
 * @create: 2022-05-28
 **/
public class Constants implements Serializable {

    private static final long serialVersionUID = 6582985503920120895L;

    //防重复提交的用户标识
    public static final String REPEAT_LIMIT_USERID = "repeatLimitUserId";

}
