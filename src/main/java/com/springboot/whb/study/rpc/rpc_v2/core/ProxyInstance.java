package com.springboot.whb.study.rpc.rpc_v2.core;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * @author: whb
 * @date: 2019/8/19 9:56
 * @description: 客户端代理对象
 */
@Slf4j
public class ProxyInstance implements InvocationHandler {

    /**
     * RPC调用方
     */
    private RpcClient rpcClient;

    private Class clazz;

    public ProxyInstance(RpcClient client, Class clazz) {
        this.rpcClient = client;
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();
        request.setClassName(clazz.getName());
        request.setMethodName(method.getName());
        request.setArguments(args);
        request.setParameterTypes(method.getParameterTypes());

        //获取服务提供方信息
        InetSocketAddress address = rpcClient.discovery(request);
        log.info("[" + Thread.currentThread().getName() + "] discovery service: " + address);

        //发起网络请求，得到请求数据
        RpcResponse response = rpcClient.invoke(request, address);
        return response.getResult();
    }
}
