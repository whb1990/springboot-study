package com.springboot.whb.study.rpc.rpc_v2.register;

import com.springboot.whb.study.rpc.rpc_v2.config.BasicConfig;
import com.springboot.whb.study.rpc.rpc_v2.core.RpcRequest;
import com.springboot.whb.study.rpc.rpc_v2.domain.ServiceType;

import java.net.InetSocketAddress;

/**
 * @author: whb
 * @date: 2019/8/19 9:59
 * @description: 服务注册
 */
public interface ServiceRegister {

    /**
     * 服务注册
     *
     * @param config
     */
    void register(BasicConfig config);

    /**
     * 服务发现，从注册中心获取可用的服务提供方信息
     *
     * @param request
     * @param nodeType
     * @return
     */
    InetSocketAddress discovery(RpcRequest request, ServiceType nodeType);
}
