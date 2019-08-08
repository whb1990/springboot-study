package com.springboot.whb.study.service;

import com.springboot.whb.study.common.annotation.CacheLock;
import com.springboot.whb.study.common.annotation.LockedObject;

/**
 * @author: whb
 * @date: 2019/8/8 15:37
 * @description: Jedis实现秒杀
 */
public interface SeckillInterface {

    @CacheLock(lockedPrefix = "TEST_PREFIX")
    void secKill(String arg1, @LockedObject Long arg2);
}
