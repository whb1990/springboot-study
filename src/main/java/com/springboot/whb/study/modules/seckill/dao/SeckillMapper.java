package com.springboot.whb.study.modules.seckill.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.springboot.whb.study.modules.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 秒杀库存表 Mapper 接口
 * </p>
 *
 * @author whb123
 * @since 2019-07-15
 */
@Repository
public interface SeckillMapper extends BaseMapper<Seckill> {

    /**
     * 查询所有的库存
     *
     * @return
     */
    List<Seckill> findAll();

    /**
     * 获取指定商品的库存数量
     *
     * @param seckillId 商品库存ID
     * @return
     */
    Integer getNumberBySeckillId(Long seckillId);

    /**
     * 数据库悲观锁获取指定商品库存数量
     *
     * @param seckillId
     * @return
     */
    Integer getNumberByDbPessimisticLock(Long seckillId);

    /**
     * 通过数据库悲观锁更新库存
     *
     * @param seckillId
     * @return
     */
    Integer updateSeckillByDbPessimisticLock(Long seckillId);

    /**
     * 数据库乐观锁更新库存
     *
     * @param seckillId 商品库存ID
     * @param number    库存数量
     * @param version   版本号
     * @return
     */
    Integer updateSeckillByDbOptimisticLock(@Param("seckillId") Long seckillId, @Param("number") Integer number, @Param("version") Integer version);
}
