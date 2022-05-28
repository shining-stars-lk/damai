package com.example.distributecache.distributedlock.redisson;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: distribute-cache
 * @description: 将yml的配置和此对象绑定
 * @author: lk
 * @create: 2022-05-28
 **/
@ConfigurationProperties(prefix = RedissonProperties.PREFIX)
public class RedissonProperties {

    public static final String PREFIX = "redisson";

    private int timeout = 3000;

    private String address = "";

    private String port = "6379";

    private String password = "";

    private int database = 2;

    private int connectionPoolSize = 64;

    private int connectionMinimumIdleSize = 10;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }

    public void setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public int getConnectionMinimumIdleSize() {
        return connectionMinimumIdleSize;
    }

    public void setConnectionMinimumIdleSize(int connectionMinimumIdleSize) {
        this.connectionMinimumIdleSize = connectionMinimumIdleSize;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "RedissonProperties{" +
                "timeout=" + timeout +
                ", address='" + address + '\'' +
                ", port='" + port + '\'' +
                ", password='" + password + '\'' +
                ", database=" + database +
                ", connectionPoolSize=" + connectionPoolSize +
                ", connectionMinimumIdleSize=" + connectionMinimumIdleSize +
                '}';
    }
}
