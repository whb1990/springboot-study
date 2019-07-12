package com.springboot.whb.study.modules.seckill.service.impl;

import com.springboot.whb.study.common.Result;
import com.springboot.whb.study.modules.seckill.service.ISeckillDistributedService;

/**
 * @author: whb
 * @date: 2019/7/12 15:04
 * @description: 秒杀-分布式锁方式
 */
public class SeckillDistributedServiceImpl implements ISeckillDistributedService {

    @Override
    public Result startSeckilRedisLock(long seckillId, long userId) {
        return null;
    }

    @Override
    public Result startSeckilZksLock(long seckillId, long userId) {
        return null;
    }

    @Override
    public Result startSeckilLock(long seckillId, long userId, long number) {
        return null;
    }
}
