package com.springboot.whb.study.distributedLock.redis;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author: whb
 * @date: 2019/7/12 21:37
 * @description: Redisson分布式锁接口定义
 */
public interface RedissonDistributedLock {
    /**
     * 加锁
     *
     * @param lockKey
     * @return
     */
    RLock lock(String lockKey);

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    RLock lock(String lockKey, int timeout);

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeUnit 时间单位
     * @param timeout  超时时间
     */
    RLock lock(String lockKey, int timeout, TimeUnit timeUnit);

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @param timeUnit  时间单位
     * @return
     */
    boolean tryLock(String lockKey, int waitTime, int leaseTime, TimeUnit timeUnit);

    /**
     * 释放锁
     *
     * @param lockKey
     */
    void unlock(String lockKey);

    /**
     * 释放锁
     *
     * @param lock
     */
    void unlock(RLock lock);
}
