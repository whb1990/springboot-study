package com.springboot.whb.study.distributedLock.redis;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author: whb
 * @date: 2019/7/12 21:34
 * @description: Redisson分布式锁
 */
public class RedissonLockUtil {
    private static RedissonDistributedLock redissLock;

    public static void setLocker(RedissonDistributedLock locker) {
        redissLock = locker;
    }

    /**
     * 加锁
     *
     * @param lockKey
     * @return
     */
    public static RLock lock(String lockKey) {
        return redissLock.lock(lockKey);
    }

    /**
     * 释放锁
     *
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        redissLock.unlock(lockKey);
    }

    /**
     * 释放锁
     *
     * @param lock
     */
    public static void unlock(RLock lock) {
        redissLock.unlock(lock);
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public static RLock lock(String lockKey, int timeout) {
        return redissLock.lock(lockKey, timeout);
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param unit    时间单位
     * @param timeout 超时时间
     */
    public static RLock lock(String lockKey, int timeout, TimeUnit unit) {
        return redissLock.lock(lockKey, timeout, unit);
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        return redissLock.tryLock(lockKey, waitTime, leaseTime, TimeUnit.SECONDS);
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param unit      时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime, TimeUnit unit) {
        return redissLock.tryLock(lockKey, waitTime, leaseTime, unit);
    }

    public static RedissonDistributedLock getRedissLock() {
        return redissLock;
    }

    public static void setRedissLock(RedissonDistributedLock redissLock) {
        RedissonLockUtil.redissLock = redissLock;
    }

}
