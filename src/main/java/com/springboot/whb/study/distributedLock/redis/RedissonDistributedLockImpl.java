package com.springboot.whb.study.distributedLock.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author: whb
 * @date: 2019/7/12 21:36
 * @description: Redisson分布式锁接口实现类
 */
public class RedissonDistributedLockImpl implements RedissonDistributedLock {

    private RedissonClient redissonClient;

    /**
     * 加锁
     *
     * @param lockKey
     * @return
     */
    @Override
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    @Override
    public RLock lock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeUnit 时间单位
     * @param timeout  超时时间
     */
    @Override
    public RLock lock(String lockKey, int timeout, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, timeUnit);
        return lock;
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @param timeUnit  时间单位
     * @return
     */
    @Override
    public boolean tryLock(String lockKey, int waitTime, int leaseTime, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param lockKey
     */
    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    /**
     * 释放锁
     *
     * @param lock
     */
    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}
