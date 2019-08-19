package com.springboot.whb.study.rpc.rpc_v2.demo;

/**
 * @author: whb
 * @date: 2019/8/19 9:57
 * @description: 测试接口定义
 */
public interface Calculate<T> {

    /**
     * 求和
     *
     * @param a
     * @param b
     * @return
     */
    T add(T a, T b);

    /**
     * 求差
     *
     * @param a
     * @param b
     * @return
     */
    T sub(T a, T b);
}
