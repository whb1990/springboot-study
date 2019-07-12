package com.springboot.whb.study.modules.seckill.repository;

import com.springboot.whb.study.modules.seckill.po.Seckill;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: whb
 * @date: 2019/7/12 16:36
 * @description: 持久层
 */
public interface SeckillRepository extends JpaRepository<Seckill, Long> {
}
