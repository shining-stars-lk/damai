package com.luo.delayqueue;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfigProperties {
    private String password;
    private cluster cluster;
    private String host;
    private String port;

    public static class cluster {
        private List<String> nodes;

        public List<String> getNodes() {
            return nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RedisConfigProperties.cluster getCluster() {
        return cluster;
    }

    public void setCluster(RedisConfigProperties.cluster cluster) {
        this.cluster = cluster;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
