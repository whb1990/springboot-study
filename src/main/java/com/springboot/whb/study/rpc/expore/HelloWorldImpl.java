package com.springboot.whb.study.rpc.expore;

import java.util.Random;

/**
 * @author: whb
 * @date: 2019/8/15 14:47
 * @description: 接口实现类
 */
public class HelloWorldImpl implements HelloWorld {

    @Override
    public String hi() {
        return "ok";
    }

    @Override
    public int add(int a, int b) {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(new Random().nextInt(10000));
            // 故意添加了耗时操作，以便于模拟真实的调用操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int c = a + b;
        System.out.println(Thread.currentThread().getName() + " 耗时:" + (System.currentTimeMillis() - start));
        return c;
    }
}
