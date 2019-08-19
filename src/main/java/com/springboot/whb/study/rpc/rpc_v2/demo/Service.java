package com.springboot.whb.study.rpc.rpc_v2.demo;

import com.springboot.whb.study.rpc.rpc_v2.core.RpcService;

/**
 * @author: whb
 * @date: 2019/8/19 9:58
 * @description: 测试服务端
 */
public class Service {
    public static void main(String[] args) {
        RpcService rpcService = new RpcService(10001);
        rpcService.addService(Calculate.class, new SimpleCalculate());

        rpcService.start();
    }
}
