package com.springboot.whb.study.modules.seckill.service.impl;

import com.springboot.whb.study.common.Result;
import com.springboot.whb.study.modules.seckill.po.Seckill;
import com.springboot.whb.study.modules.seckill.service.ISeckillService;

import java.util.List;

/**
 * @author: whb
 * @date: 2019/7/12 15:00
 * @description: 秒杀商品接口实现
 */
public class SeckillServiceImpl implements ISeckillService {

    @Override
    public List<Seckill> getSeckillList() {
        return null;
    }

    @Override
    public Seckill getById(long seckillId) {
        return null;
    }

    @Override
    public Long getSeckillCount(long seckillId) {
        return null;
    }

    @Override
    public void deleteSeckill(long seckillId) {

    }

    @Override
    public Result startSeckil(long seckillId, long userId) {
        return null;
    }

    @Override
    public Result startSeckilLock(long seckillId, long userId) {
        return null;
    }

    @Override
    public Result startSeckilAopLock(long seckillId, long userId) {
        return null;
    }

    @Override
    public Result startSeckilDBPCC_ONE(long seckillId, long userId) {
        return null;
    }

    @Override
    public Result startSeckilDBPCC_TWO(long seckillId, long userId) {
        return null;
    }

    @Override
    public Result startSeckilDBOCC(long seckillId, long userId, long number) {
        return null;
    }

    @Override
    public Result startSeckilTemplate(long seckillId, long userId, long number) {
        return null;
    }
}
