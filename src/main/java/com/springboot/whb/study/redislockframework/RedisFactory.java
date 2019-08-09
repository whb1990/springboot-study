package com.springboot.whb.study.redislockframework;

import redis.clients.jedis.JedisPool;

public class RedisFactory {

    public static RedisClient getDefaultClient() {
        JedisPool pool = new JedisPool("127.0.0.1");
        RedisClient client = new RedisClient(pool);
        return client;
    }
}
