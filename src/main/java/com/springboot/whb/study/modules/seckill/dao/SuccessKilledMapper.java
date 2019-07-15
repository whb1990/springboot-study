package com.springboot.whb.study.modules.seckill.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.springboot.whb.study.modules.seckill.entity.SuccessKilled;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 秒杀成功明细表 Mapper 接口
 * </p>
 *
 * @author whb123
 * @since 2019-07-15
 */
@Repository
public interface SuccessKilledMapper extends BaseMapper<SuccessKilled> {

    /**
     * 查询指定商品的秒杀记录数
     *
     * @param seckillId
     * @return
     */
    Long getCountBySeckillId(Long seckillId);

    void deleteBySeckillId(Long seckillId);
}
