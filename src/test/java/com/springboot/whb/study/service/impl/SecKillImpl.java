package com.springboot.whb.study.service.impl;

import com.springboot.whb.study.service.SeckillInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: whb
 * @date: 2019/8/8 15:38
 * @description: Jedis实现秒杀接口实现类
 */
public class SecKillImpl implements SeckillInterface {
    //商品
    public static Map<Long, Long> inventory;

    static {
        inventory = new HashMap<>();
        inventory.put(10000001L, 10000l);
        inventory.put(10000002L, 10000l);
    }

    @Override
    public void secKill(String arg1, Long arg2) {
        reduceInventory(arg2);
    }

    //模拟秒杀操作，姑且认为一个秒杀就是将库存减一，实际情景要复杂的多
    public Long reduceInventory(Long commodityId) {
        inventory.put(commodityId, inventory.get(commodityId) - 1);
        return inventory.get(commodityId);
    }
}
