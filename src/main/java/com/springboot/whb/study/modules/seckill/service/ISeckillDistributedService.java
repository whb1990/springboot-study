package com.springboot.whb.study.modules.seckill.service;

import com.springboot.whb.study.common.Result;

/**
 * @author: whb
 * @date: 2019/7/12 15:02
 * @description: 秒杀--分布式锁
 */
public interface ISeckillDistributedService {

    /**
     * 秒杀 一  单个商品
     *
     * @param seckillId 秒杀商品ID
     * @param userId    用户ID
     * @return
     */
    Result startSeckilRedisLock(long seckillId, long userId);

    /**
     * 秒杀 一  单个商品
     *
     * @param seckillId 秒杀商品ID
     * @param userId    用户ID
     * @return
     */
    Result startSeckilZksLock(long seckillId, long userId);

    /**
     * 秒杀 二 多个商品
     *
     * @param seckillId 秒杀商品ID
     * @param userId    用户ID
     * @param number    秒杀商品数量
     * @return
     */
    Result startSeckilLock(long seckillId, long userId, long number);
}
