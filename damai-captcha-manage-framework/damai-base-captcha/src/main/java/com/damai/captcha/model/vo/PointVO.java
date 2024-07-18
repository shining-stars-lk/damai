package com.damai.captcha.model.vo;

import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 返回位置Vo
 * @author: 阿星不是程序员
 **/
@Data
public class PointVO {
    private String secretKey;

    public int x;

    public int y;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public PointVO(int x, int y, String secretKey) {
        this.secretKey = secretKey;
        this.x = x;
        this.y = y;
    }

    public PointVO() {
    }

    public PointVO(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toJsonString() {
        return String.format("{\"secretKey\":\"%s\",\"x\":%d,\"y\":%d}", secretKey, x, y);
    }

    public PointVO parse(String jsonStr) {
        Map<String, Object> m = new HashMap(64);
        Arrays.stream(jsonStr
                .replaceFirst(",\\{", "\\{")
                .replaceFirst("\\{", "")
                .replaceFirst("\\}", "")
                .replaceAll("\"", "")
                .split(",")).forEach(item -> {
            m.put(item.split(":")[0], item.split(":")[1]);
        });
        //PointVO d = new PointVO();
        setX(Double.valueOf(String.valueOf(m.getOrDefault("x","0"))).intValue());
        setY(Double.valueOf(String.valueOf(m.getOrDefault("y","0"))).intValue());
        setSecretKey(String.valueOf(m.getOrDefault("secretKey", "")));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PointVO pointVO = (PointVO) o;
        return x == pointVO.x && y == pointVO.y && Objects.equals(secretKey, pointVO.secretKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(secretKey, x, y);
    }
}
