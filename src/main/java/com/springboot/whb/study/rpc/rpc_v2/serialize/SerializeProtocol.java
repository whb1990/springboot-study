package com.springboot.whb.study.rpc.rpc_v2.serialize;

/**
 * @author: whb
 * @date: 2019/8/19 10:45
 * @description: 序列化协议接口
 */
public interface SerializeProtocol {

    /**
     * 序列化
     */
    <T> byte[] serialize(Class<T> clazz, T t);

    /**
     * 反序列化
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
