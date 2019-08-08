package com.springboot.whb.study.distributedlock.redis.jedis;

import com.springboot.whb.study.distributedlock.redis.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: whb
 * @date: 2019/8/8 11:22
 * @description:
 */
@Service
public class JedisFactory {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisPool getJedisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
        jedisPoolConfig.setMaxTotal(redisProperties.getMaxActive());
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getMaxWait());
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisProperties.getHost(), redisProperties.getPort(), redisProperties.getTimeout());
        return jedisPool;
    }
}
