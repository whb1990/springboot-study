package com.springboot.whb.study.rpc.rpc_v2.demo;

import java.util.Random;

/**
 * @author: whb
 * @date: 2019/8/19 9:58
 * @description: 测试接口实现类
 */
public class SimpleCalculate implements Calculate<Integer> {

    @Override
    public Integer add(Integer a, Integer b) {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int c = a + b;
        System.out.println(Thread.currentThread().getName() + " 耗时:" + (System.currentTimeMillis() - start));
        return c;
    }

    @Override
    public Integer sub(Integer a, Integer b) {
        return a - b;
    }
}
