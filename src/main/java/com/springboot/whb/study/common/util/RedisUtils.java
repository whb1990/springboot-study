package com.springboot.whb.study.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: whb
 * @date: 2019/7/11 16:16
 * @description: Redis工具类
 */
@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ValueOperations<String, String> valueOperations;
    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;
    private final static Gson gson = new Gson();

    public void set(String key, Object value, long expire) {
        valueOperations.set(key, toJson(value));
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void incr(String key) {
        valueOperations.increment(key, 1);
    }

    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }


    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public <T> List<T> getList(String key, Class<T> clazz, long expire) {
        String value = (String) this.valueOperations.get(key);
        if (expire != -1L) {
            this.redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }

        return value == null ? null : jsonToBeanList(value, clazz);
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        return this.getList(key, clazz, -1L);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> List<T> jsonToBeanList(String json, Class<T> t) {
        List<T> list = new ArrayList();
        JsonParser parser = new JsonParser();
        JsonArray jsonarray = parser.parse(json).getAsJsonArray();
        Iterator var5 = jsonarray.iterator();

        while (var5.hasNext()) {
            JsonElement element = (JsonElement) var5.next();
            list.add(gson.fromJson(element, t));
        }
        return list;
    }
}
