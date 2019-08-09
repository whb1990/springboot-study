package com.springboot.whb.study.redislockframework;

/**
 * 模拟秒杀，接口定义
 */
public interface SeckillInterface {

    @CacheLock(lockedPrefix = "TEST_PREFIX")
    void secKill(String arg1, @LockedObject Long arg2);
}
