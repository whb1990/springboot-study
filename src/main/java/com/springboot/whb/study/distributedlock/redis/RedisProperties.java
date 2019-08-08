package com.springboot.whb.study.distributedlock.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author: whb
 * @date: 2019/8/8 16:23
 * @description: Redis配置
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 超时时间
     */
    private int timeout;

    /**
     * 连接池最大线程数
     */
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    /**
     * 等待时间
     */
    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWait;

    /**
     * 最大空闲连接
     */
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }
}
