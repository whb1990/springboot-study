package com.springboot.whb.study.rpc.rpc_v1;

import com.springboot.whb.study.rpc.rpc_v1.expore.HelloWorldImpl;

/**
 * @author: whb
 * @date: 2019/8/15 14:46
 * @description: 服务端
 */
public class Service {

    public static void main(String[] args) {
        RpcExploreService rpcExploreService = new RpcExploreService();
        //传入的字符串是接口的全名称
        rpcExploreService.explore("com.springboot.whb.study.rpc.rpc_v1.expore.HelloWorld", new HelloWorldImpl());
        try {
            //开启11111端口监听服务
            Runnable ioService = new IOService(rpcExploreService, 11111);
            new Thread(ioService).start();
        } catch (Exception e) {

        }
    }
}
