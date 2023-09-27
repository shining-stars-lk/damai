package com.example.util;


import com.example.model.ExceptionNode;
import com.example.model.InvokedInfo;
import com.example.model.MethodNode;
import com.example.service.InvokedQueue;
import com.example.service.MethodNodeService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class KoUtil {
    private final static String koTimeSecret = UUID.randomUUID().toString().replace("-", "");

    private final static List<Integer> choices = randomSecretIndexs();

    private static Map<String, Object> caches = new HashMap<>();

    /**
     * nothing to introduce for this, that everyone knows!
     */
    public static String login(String userName) {
        String token = encode(userName + "KOTO" + (System.currentTimeMillis() + (Context.getConfig().getAuthExpire() * 1000)));
        return token;
    }

    /**
     * nothing to introduce for this, that everyone knows!
     */
    public static void checkLogin(String token) {
        decode(token);
    }

    /**
     * nothing to introduce for this, that everyone knows!
     */
    public static boolean isLogin(String token) {
        try {
            decode(token);
            return true;
        } catch (Exception verificationException) {
            return false;
        }
    }

    /**
     * set a Datasource for saving of ko-time data
     * note: this Datasource will not affect project's datasource
     */
    public static void setDataSource(DataSource dataSource) {
        caches.put("dataSource", dataSource);
    }

    public static DataSource getDataSource() {
        return (DataSource) caches.get("dataSource");
    }

    /**
     * set a RedisTemplate for saving of ko-time data
     * note: you can choose one between setRedisTemplate and setJedisPool to save data
     */
    public static void setStringRedisTemplate(RedisTemplate redisTemplate) {
        caches.put("redisTemplate", redisTemplate);
    }

    public static StringRedisTemplate getStringRedisTemplate() {
        return (StringRedisTemplate) caches.get("redisTemplate");
    }


    /**
     * throw the exception to ko-time
     * this method will throw an exception named RecordException,and RunTimeHandler will receive it so that it can be record by ko-time
     */
    public static void throwException(Exception e) {
        ThrowException throwException = new ThrowException(e);
        throw throwException;
    }

    /**
     * record the exception to ko-time
     */
    public static void recordException(Exception e) {
        ExceptionNode exception = new ExceptionNode();
        exception.setName(e.getClass().getSimpleName());
        exception.setClassName(e.getClass().getName());
        exception.setMessage(e.getMessage() + "");
        exception.setId(exception.getClassName() + "." + exception.getName());
        MethodNode current = getCurrentMethodInfo();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            if (stackTraceElement.getClassName().equals(current.getClassName())) {
                exception.setValue(stackTraceElement.getLineNumber());
                InvokedInfo invokedInfo = new InvokedInfo();
                invokedInfo.setCurrent(current);
                invokedInfo.setException(exception);
                InvokedQueue.add(invokedInfo);
                InvokedQueue.wake();
                break;
            }
        }
    }

    /**
     * get the current method
     *
     * @return
     */
    public static MethodNode getCurrentMethodInfo() {
        MethodNode methodNode = MethodNodeService.getParentMethodNode();
        return methodNode;
    }


    public static void clearCaches() {
        caches.clear();
    }


    private static List<Integer> randomSecretIndexs() {
        List<Integer> choices = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int intrandom = random.nextInt(20);
            if (choices.contains(intrandom)) {
                continue;
            }
            choices.add(intrandom);
        }
        return choices;
    }


    private static String encode(String text) {
        Base64.Encoder encoder = Base64.getEncoder();
        String encode = encoder.encodeToString(text.getBytes());
        int choicesSize = choices.size();
        for (int i = 0; i < choicesSize; i++) {
            Integer choice = choices.get(i);
            String pre = encode.substring(0, choice);
            String suf = encode.substring(choice);
            encode = pre + koTimeSecret.substring(i, i + 1) + suf;
        }
        return encode;
    }

    private static String decode(String token) {
        int tokenLength = token.length();
        int choicesSize = choices.size();
        for (int i = choicesSize - 1; i >= 0; i--) {
            Integer choice = choices.get(i);
            String pre = token.substring(0, choice);
            String suf = token.substring(choice + 1);
            String secretAt = koTimeSecret.substring(i, i + 1);
            if ((choice + 1) > tokenLength) {
                throw new InvalidAuthInfoException("error token!");
            }
            String tokenAt = token.substring(choice, choice + 1);
            if (!secretAt.equals(tokenAt)) {
                throw new InvalidAuthInfoException("error token!");
            }
            token = pre + suf;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(token);
        String decodeStr = new String(decode);
        String[] split = decodeStr.split("KOTO");
        Long expireTime = Long.valueOf(split[1]);
        if (expireTime < System.currentTimeMillis()) {
            throw new InvalidAuthInfoException("expired time!");
        }
        return decodeStr;
    }

    public static void setProperty(String propertyName, String propertyValue) {
        Context.getDynamicProperties().setProperty(propertyName, propertyValue);
    }

    public static String getProperty(String propertyName) {
        String value = Context.getDynamicProperties().getProperty(propertyName);
        return value;
    }

    public static int getPropertyAsInteger(String propertyName) {
        String value = getProperty(propertyName);
        return Integer.valueOf(value);
    }

    public static double getPropertyAsDouble(String propertyName) {
        String value = getProperty(propertyName);
        return Double.valueOf(value);
    }

    public static double getPropertyAsFloat(String propertyName) {
        String value = getProperty(propertyName);
        return Float.valueOf(value);
    }

    public static boolean getPropertyAsBoolean(String propertyName) {
        String value = getProperty(propertyName);
        if ("true".equals(value) || "false".equals(value)) {
            return Boolean.valueOf(value);
        }
        throw new RuntimeException("can not convert null value to boolean value.");
    }

    public static List<String> getPropertyAsList(String propertyName, String split) {
        String value = getProperty(propertyName);
        if (value == null) {
            throw new RuntimeException("can not convert null value to list values.");
        }
        String[] split1 = value.split(split);
        return Arrays.stream(split1).collect(Collectors.toList());
    }


}
