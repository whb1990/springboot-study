package com.springboot.whb.study.modules.seckill.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.springboot.whb.study.common.Result;
import com.springboot.whb.study.common.annotation.LockService;
import com.springboot.whb.study.common.annotation.RateLimit;
import com.springboot.whb.study.common.enums.SeckillStateEnum;
import com.springboot.whb.study.modules.seckill.dao.SeckillMapper;
import com.springboot.whb.study.modules.seckill.dao.SuccessKilledMapper;
import com.springboot.whb.study.modules.seckill.entity.Seckill;
import com.springboot.whb.study.modules.seckill.entity.SuccessKilled;
import com.springboot.whb.study.modules.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 秒杀库存表 服务实现类
 * </p>
 *
 * @author whb123
 * @since 2019-07-15
 */
@Service("seckillService")
@Slf4j
public class SeckillServiceImpl extends ServiceImpl<SeckillMapper, Seckill> implements SeckillService {

    @Autowired
    private SuccessKilledMapper successKilledMapper;

    /**
     * 公平锁
     */
    private Lock lock = new ReentrantLock(true);

    /**
     * 查询全部的秒杀记录
     *
     * @return
     */
    @Override
    public List<Seckill> getSeckillList() {
        return baseMapper.findAll();
    }

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    @Override
    public Seckill getById(Long seckillId) {
        return baseMapper.selectById(seckillId);
    }

    /**
     * 查询秒杀售卖商品
     *
     * @param seckillId
     * @return
     */
    @Override
    public Long getSeckillCount(Long seckillId) {
        return successKilledMapper.getCountBySeckillId(seckillId);
    }

    /**
     * 删除秒杀售卖商品记录
     *
     * @param seckillId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSeckill(Long seckillId) {
        successKilledMapper.deleteBySeckillId(seckillId);
        Seckill seckill = new Seckill();
        seckill.setSeckillId(seckillId);
        seckill.setNumber(100);
        baseMapper.updateById(seckill);
    }

    /**
     * 秒杀 一、会出现数量错误
     *
     * @param seckillId
     * @param userId
     * @return
     */
    @Override
    @RateLimit
    @Transactional
    public Result startSeckil(Long seckillId, Long userId) {
        return successKilled(seckillId, userId, false);
    }

    /**
     * 秒杀 二、程序锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public Result startSeckilLock(Long seckillId, Long userId) {
        try {
            lock.lock();
            return successKilled(seckillId, userId, false);
        } catch (Exception e) {
            log.error("通过程序锁秒杀失败，seckillId:{},userId:{},错误：{}", seckillId, userId, e);
        } finally {
            lock.unlock();
        }
        return Result.error(SeckillStateEnum.END);
    }

    /**
     * 秒杀 二、程序锁AOP
     *
     * @param seckillId
     * @param userId
     * @return
     */
    @Override
    @LockService
    @Transactional
    public Result startSeckilAopLock(Long seckillId, Long userId) {
        return successKilled(seckillId, userId, false);
    }

    /**
     * 秒杀 二、数据库悲观锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    @Override
    @RateLimit
    @Transactional
    public Result startSeckilDBPCC_ONE(Long seckillId, Long userId) {
        return successKilled(seckillId, userId, true);
    }

    /**
     * 秒杀 三、数据库悲观锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public Result startSeckilDBPCC_TWO(Long seckillId, Long userId) {
        int count = baseMapper.updateSeckillByDbPessimisticLock(seckillId);
        if (count > 0) {
            //创建订单
            SuccessKilled killed = wrapSuccessKilled(seckillId, userId);
            successKilledMapper.insert(killed);
            return Result.ok(SeckillStateEnum.SUCCESS);
        }
        return Result.error(SeckillStateEnum.END);
    }

    /**
     * 秒杀 三、数据库乐观锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public Result startSeckilDBOCC(Long seckillId, Long userId, Integer number) {
        Seckill seckill = baseMapper.selectById(seckillId);
        //剩余的数量应该要大于等于秒杀的数量
        if (seckill.getNumber() >= number) {
            //数据库乐观锁
            int count = baseMapper.updateSeckillByDbOptimisticLock(seckillId, number, seckill.getVersion());
            if (count > 0) {
                //创建订单
                SuccessKilled killed = wrapSuccessKilled(seckillId, userId);
                successKilledMapper.insert(killed);
                return Result.ok(SeckillStateEnum.SUCCESS);
            }
        }
        return Result.error(SeckillStateEnum.END);
    }

    /**
     * @param seckillId     秒杀库存ID
     * @param userId        秒杀用户
     * @param dbPessimistic 是否数据库悲观锁
     * @return
     */
    private Result successKilled(Long seckillId, Long userId, boolean dbPessimistic) {
        //校验库存
        Integer number = dbPessimistic ? baseMapper.getNumberByDbPessimisticLock(seckillId) : baseMapper.getNumberBySeckillId(seckillId);
        if (number != null && number > 0) {
            //扣库存
            Seckill seckill = new Seckill();
            seckill.setSeckillId(seckillId);
            seckill.setNumber(number - 1);
            baseMapper.updateById(seckill);
            //创建订单
            SuccessKilled killed = wrapSuccessKilled(seckillId, userId);
            successKilledMapper.insert(killed);
            return Result.ok(SeckillStateEnum.SUCCESS);
        }
        return Result.error(SeckillStateEnum.END);
    }

    /**
     * 封装秒杀订单
     *
     * @param seckillId 商品库存ID
     * @param userId    用户ID
     * @return
     */
    private SuccessKilled wrapSuccessKilled(Long seckillId, Long userId) {
        SuccessKilled killed = new SuccessKilled();
        killed.setSeckillId(seckillId);
        killed.setUserId(userId);
        killed.setState(0);
        return killed;
    }
}
