package com.springboot.whb.study.rpc.rpc_v2.core;

import com.springboot.whb.study.rpc.rpc_v2.config.ClientConfig;
import com.springboot.whb.study.rpc.rpc_v2.domain.ServiceType;
import com.springboot.whb.study.rpc.rpc_v2.io.protocol.DefaultMessageProtocol;
import com.springboot.whb.study.rpc.rpc_v2.register.ServiceRegister;
import com.springboot.whb.study.rpc.rpc_v2.register.ZkServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: whb
 * @date: 2019/8/19 9:56
 * @description: RPC客户端
 */
public class RpcClient {
    /**
     * k 是接口的全名称
     * v 是对应的对象包含的详细信息
     */
    private Map<String, ClientConfig> clientConfigMap = new HashMap<>();

    /**
     * 服务注册
     */
    private ServiceRegister serviceRegister;

    /**
     * 客户端处理器
     */
    private ClientHandler clientHandler;

    public RpcClient() {
        this.serviceRegister = new ZkServiceRegister();
        this.clientHandler = new ClientHandler(this);
        // 设置默认的消息处理协议
        this.clientHandler.setMessageProtocol(new DefaultMessageProtocol());
    }

    /**
     * 订阅服务
     *
     * @param clazz
     * @param <T>
     */
    public <T> void subscribe(Class<T> clazz) {
        String interfaceName = clazz.getName();
        ProxyInstance invocationHandler = new ProxyInstance(this, clazz);
        ClientConfig<T> clientConfig = ClientConfig.convert(clazz, invocationHandler);
        clientConfigMap.put(interfaceName, clientConfig);
    }

    /**
     * 服务注册
     */
    private void register() {
        // 服务注册，在网络监听启动之前就需要完成
        clientConfigMap.values().forEach(serviceRegister::register);
    }

    /**
     * 服务启动
     */
    public void start() {
        this.register();
    }

    /**
     * 服务发现
     *
     * @param request
     * @return
     */
    public InetSocketAddress discovery(RpcRequest request) {
        return serviceRegister.discovery(request, ServiceType.PROVIDER);
    }

    /**
     * 反射调用
     *
     * @param request
     * @param address
     * @return
     */
    public RpcResponse invoke(RpcRequest request, InetSocketAddress address) {
        return this.clientHandler.invoke(request, address);
    }

    /**
     * 获取对象实例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clazz) {
        return (T) (clientConfigMap.get(clazz.getName()).getProxy());
    }

}
