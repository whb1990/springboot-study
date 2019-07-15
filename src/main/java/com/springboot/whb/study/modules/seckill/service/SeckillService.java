package com.springboot.whb.study.modules.seckill.service;

import com.baomidou.mybatisplus.service.IService;
import com.springboot.whb.study.common.Result;
import com.springboot.whb.study.modules.seckill.entity.Seckill;

import java.util.List;

/**
 * <p>
 * 秒杀库存表 服务类
 * </p>
 *
 * @author whb123
 * @since 2019-07-15
 */
public interface SeckillService extends IService<Seckill> {
    /**
     * 查询全部的秒杀记录
     *
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    Seckill getById(Long seckillId);

    /**
     * 查询秒杀售卖商品
     *
     * @param seckillId
     * @return
     */
    Long getSeckillCount(Long seckillId);

    /**
     * 删除秒杀售卖商品记录
     *
     * @param seckillId
     * @return
     */
    void deleteSeckill(Long seckillId);

    /**
     * 秒杀 一、会出现数量错误
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckil(Long seckillId, Long userId);

    /**
     * 秒杀 二、程序锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckilLock(Long seckillId, Long userId);

    /**
     * 秒杀 二、程序锁AOP
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckilAopLock(Long seckillId, Long userId);

    /**
     * 秒杀 二、数据库悲观锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckilDBPCC_ONE(Long seckillId, Long userId);

    /**
     * 秒杀 三、数据库悲观锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckilDBPCC_TWO(Long seckillId, Long userId);

    /**
     * 秒杀 三、数据库乐观锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckilDBOCC(Long seckillId, Long userId, Integer number);
}
