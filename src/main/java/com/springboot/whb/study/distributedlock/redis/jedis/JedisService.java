package com.springboot.whb.study.distributedlock.redis.jedis;

import com.alibaba.fastjson.JSON;
import com.springboot.whb.study.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.*;

/**
 * @author: whb
 * @date: 2019/8/8 16:51
 * @description: Jedis接口
 */
@Slf4j
@Service
public class JedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 根据key来获取对应的value
     */
    public Object getByKey(String key) {
        Jedis jedis = null;
        Object object = null;
        try {
            jedis = jedisPool.getResource();
            object = jedis.get(key);
            return object;
        } catch (Exception e) {
            log.error("redis连接池异常，根据Key获取对应的value执行报错，key:{},error:{}", key, e);
            return null;
        } finally {
            jedisPool.close();
        }
    }

    /**
     * 判断String类型key是否存在
     */
    public boolean isKeyExist(String key) {
        Jedis jedis = null;
        boolean exist = false;
        try {
            jedis = jedisPool.getResource();
            exist = jedis.exists(key);
            return exist;
        } catch (Exception e) {
            log.error("redis连接池异常，判断String类型key是否存在执行报错，key:{},error:{}", key, e);
            return false;
        } finally {
            jedisPool.close();
        }
    }

    /**
     * String类型的键值写入redis
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, String value) {
        Jedis jedis = null;
        String issuccess = "";
        try {
            jedis = jedisPool.getResource();
            issuccess = jedis.set(key, value);
            if ("OK".equals(issuccess)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("redis连接池异常，写入key:{},value:{}，执行报错，error:{}", key, value, e);
            return false;
        } finally {
            jedisPool.close();
        }
    }

    /**
     * 写入键值对
     *
     * @param key
     * @param value
     * @return
     */
    public Long setnx(String key, String value) {
        Jedis client = null;
        try {
            client = jedisPool.getResource();
            Long result = client.setnx(key, value);
            System.out.println("setnx key=" + key + " value=" + value +
                    "result=" + result);
            return result;
        } catch (Exception e) {
            log.error("redis连接池异常，向redis写入key:{},value:{}，执行报错，error:{}", key, value, e);
            return null;
        } finally {
            jedisPool.close();
        }
    }

    /**
     * String类型的键值写入redis,并设置失效时间
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setKeyWithExpireTime(String key, String value, int time) {
        if (time == 0) {

        }
        Jedis jedis = null;
        String issuccess = "";
        try {
            jedis = jedisPool.getResource();
            issuccess = jedis.setex(key, time, value);
            if ("OK".equals(issuccess)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("redis连接池异常，String类型的键值写入redis,并设置失效时间执行报错，key:{},value:{},error:{}", key, value, e);
            return false;
        } finally {
            jedisPool.close();
        }
    }

    /**
     * list<String>结构的数据写入redis
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lpush(String key, List<String> value) {
        Jedis client = null;
        try {
            client = jedisPool.getResource();
            Transaction tx = client.multi();
            for (String one : value) {
                tx.lpush(key, one);
            }
            tx.exec();
            return true;
        } catch (Exception e) {
            log.error("redis连接池异常，list<String>结构的数据写入redis执行报错，key:{},value:{},error:{}", key, value, e);
            return false;
        } finally {
            jedisPool.close();
        }
    }

    /**
     * 根据key获取list类型
     *
     * @param key
     * @return
     */
    public List<String> lrange(String key) {
        Jedis jedis = null;
        List<String> returnList = null;
        try {
            jedis = jedisPool.getResource();
            returnList = jedis.lrange(key, 0, -1);
            return returnList;
        } catch (Exception e) {
            log.error("redis连接池异常，根据key获取list类型执行报错，key:{},error:{}", key, e);
            return null;
        } finally {
            jedisPool.close();
        }
    }

    public List<String> lrange(String key, int start, int length) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.lrange(key, start, length);
        } finally {
            jedisPool.close();
        }
    }

    /**
     * @param key
     * @param o
     * @return
     */
    public boolean setAnObject(String key, Object o) {
        Jedis jedis = jedisPool.getResource();
        try {
            String afterSerialize = JSON.toJSONString(o);
            o = jedis.set(key, afterSerialize);
            return true;
        } finally {
            jedisPool.close();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getSetT(String key, T newValue) {
        Jedis jedis = jedisPool.getResource();
        T t;
        try {
            String afterSerialize = JsonUtils.beanToJson(newValue);
            String value = jedis.getSet(key, afterSerialize);
            t = (T) JsonUtils.jsonToBean(value, newValue.getClass());
            return t;
        } finally {
            jedisPool.close();
        }
    }

    public <T> T getAnObject(String key, Class<T> zz) {
        Jedis jedis = jedisPool.getResource();
        T t;
        try {
            String s = jedis.get(key);
            if (s == null || s.length() == 0) {
                return null;
            }
            t = JsonUtils.jsonToBean(s, zz);
        } finally {
            jedisPool.close();
        }
        return t;

    }

    public List<String> getKeys(String pattern) {
        Jedis jedis = jedisPool.getResource();
        List<String> result = new ArrayList<String>();
        try {
            Set<String> set = jedis.keys(pattern);
            result.addAll(set);
        } finally {
            jedisPool.close();
        }
        return result;
    }

    public boolean delKey(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            System.out.println("del key=" + key);
            jedis.del(key);
            return true;
        } finally {
            jedisPool.close();
        }
    }

    public <T> boolean hset(String key, String field, T t) {
        Jedis jedis = jedisPool.getResource();
        try {
            String afterSerialize = JsonUtils.beanToJson(t);
            jedis.hset(key, field, afterSerialize);
            return true;
        } finally {
            jedisPool.close();
        }

    }

    /**
     * 存入的时hash结构的数据
     *
     * @param key key
     * @param map map的key实质为field。
     * @return
     */
    public <T, S> boolean hmset(String key, Map<T, S> map) {
        Jedis jedis = jedisPool.getResource();
        try {
            Iterator<Map.Entry<T, S>> iterator = map.entrySet().iterator();
            Map<String, String> stringMap = new HashMap<>();
            String filed;
            String value;
            while (iterator.hasNext()) {
                Map.Entry<T, S> entry = iterator.next();
                filed = String.valueOf(entry.getKey());
                value = JsonUtils.beanToJson(entry.getValue());
                stringMap.put(filed, value);
            }
            jedis.hmset(key, stringMap);
            return true;
        } finally {
            jedisPool.close();
        }

    }

    public <T> T hgetObject(String key, String field, Class<T> cls) {
        Jedis jedis = jedisPool.getResource();
        try {
            String value = jedis.hget(key, field);
            return (T) JsonUtils.jsonToBean(value, cls);
        } finally {
            jedisPool.close();
        }

    }

    public String hgetString(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        try {
            String value = jedis.hget(key, field);
            return value;
        } finally {
            jedisPool.close();
        }

    }

    public Map<String, String> hGetAll(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hgetAll(key);
        } finally {
            jedisPool.close();
        }

    }

    public List<String> hkeys(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            List<String> fields = new ArrayList<String>();
            Set<String> set = jedis.hkeys(key);
            fields.addAll(set);
            return fields;
        } finally {
            jedisPool.close();
        }

    }

    public List<String> hvals(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            List<String> values = jedis.hvals(key);
            return values;
        } finally {
            jedisPool.close();
        }

    }

    public boolean hexists(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hexists(key, field);
        } finally {
            jedisPool.close();
        }
    }

    public long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.incr(key);
        } finally {
            jedisPool.close();
        }
    }

    public void hdel(String key, String... fields) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.hdel(key, fields);
        } finally {
            jedisPool.close();
        }
    }

    /**
     * @param key
     * @param field
     */
    public void lpush(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.lpush(key, field);
        } finally {
            jedisPool.close();
        }
    }

    public void lpush(String key, Object obj) {
        Jedis jedis = jedisPool.getResource();
        try {
            String field = JsonUtils.beanToJson(obj);
            jedis.lpush(key, field);
        } finally {
            jedisPool.close();
        }
    }

    /**
     * 该方法不适用于普通的调用，该方法只针对于错误日志记录
     *
     * @param key
     * @param field
     */
    public void lpushForErrorMsg(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        try {
            if (jedis.llen(key) > 1000) {
                return;
            }
            jedis.lpush(key, field);
        } finally {
            jedisPool.close();
        }
    }

    public long llen(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.llen(key);
        } finally {
            jedisPool.close();
        }
    }

    public List<String> blPop(String key, int timeoutSeconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.blpop(timeoutSeconds, key);
        } catch (Exception e) {
            log.error("blpop出错，error:{}", e);
            return null;
        } finally {
            jedisPool.close();
        }
    }

    public <T> long sadd(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sadd(key, values);
        } catch (Exception e) {
            log.error("sadd出错，error:{}", e);
            return -1;
        } finally {
            jedisPool.close();
        }
    }

    public <T> long sadd(String key, List<T> ts) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (ts == null || ts.size() == 0) {
                return 0L;
            }
            String[] values = new String[ts.size()];
            for (int i = 0; i < ts.size(); i++) {
                values[i] = ts.get(i).toString();
            }
            return jedis.sadd(key, values);
        } catch (Exception e) {
            log.error("sadd出错，error:{}", e);
            return -1;
        } finally {
            jedisPool.close();
        }
    }

    public long srem(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.srem(key, values);
        } catch (Exception e) {
            log.error("srem出错,error:{}", e);
            return -1;
        } finally {
            jedisPool.close();
        }
    }

    public <T> long srem(String key, List<T> ts) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (ts == null || ts.size() == 0) {
                return 0L;
            }
            String[] values = new String[ts.size()];
            for (int i = 0; i < ts.size(); i++) {
                values[i] = ts.get(i).toString();
            }
            return jedis.srem(key, values);
        } catch (Exception e) {
            log.error("srem出错，error:{}", e);
            return -1L;
        } finally {
            jedisPool.close();
        }
    }

    public Set<String> getByRange(String key, double min, double max) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("getByRange出错，error:{}", e);
            return null;
        } finally {
            jedisPool.close();
        }
    }

    public Long decr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.decr(key);
        } catch (Exception e) {
            log.error("decr出错,error:{}", e);
            return null;
        } finally {
            jedisPool.close();
        }
    }

    public Long hlen(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hlen(key);
        } catch (Exception e) {
            log.error("hlen出错,error:{}", e);
            return null;
        } finally {
            jedisPool.close();
        }
    }

    public List<String> hmget(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            log.error("hmget出错:{}", e);
            return null;
        } finally {
            jedisPool.close();
        }
    }

    /**
     * 从redis里面得到以 某字符串开头的所有key
     *
     * @param str
     */
    public Set<String> getKeyByStr(String str) {
        Jedis jedis = null;
        Set<String> keys = null;
        try {
            jedis = jedisPool.getResource();
            keys = jedis.keys(str);
            return keys;
        } catch (Exception e) {
            log.error("从redis获取以【{}】开头的所有key出错，error:{}", str, e);
            return null;
        } finally {
            jedisPool.close();
        }
    }

    public void ltrim(String key, int start, int stop) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.ltrim(key, start, stop);
        } catch (Exception e) {
            log.error("ltrim执行出错,key:{},start:{},stop:{},error:{}", key, start, stop, e);
        } finally {
            jedisPool.close();
        }
    }

    /**
     * 为指定key设置过期时间
     *
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(String key, Integer seconds) {
        Jedis jedis = null;
        Long success = 1L;
        try {
            jedis = jedisPool.getResource();
            success = jedis.expire(key, seconds);
            return success;
        } catch (Exception e) {
            log.error("为key:{}设置过期时间seconds:{}出错，error:{}", key, seconds, e);
            return null;
        } finally {
            jedisPool.close();
        }
    }

    /**
     * 存入的时hash结构的数据,并且去掉value中的引号
     *
     * @param key key
     * @param map map的key实质为field。
     * @return
     */
    public <T, S> boolean hmsetWithoutQuotationMarks(String key, Map<T, S> map) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Iterator<Map.Entry<T, S>> iterator = map.entrySet().iterator();
            Map<String, String> stringMap = new HashMap<>();
            String filed;
            String value;
            while (iterator.hasNext()) {
                Map.Entry<T, S> entry = iterator.next();
                filed = String.valueOf(entry.getKey());
                value = JSON.toJSONString(entry.getValue()).replace("\"", "");
                stringMap.put(filed, value);
            }
            jedis.hmset(key, stringMap);
            return true;
        } catch (Exception e) {
            log.error("hmsetWithoutQuotationMarks方法执行报错,key:{},map:{},error:{}", key, map, e);
            return false;
        } finally {
            jedisPool.close();
        }
    }
}
