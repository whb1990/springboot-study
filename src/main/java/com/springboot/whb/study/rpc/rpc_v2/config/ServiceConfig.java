package com.springboot.whb.study.rpc.rpc_v2.config;

import com.alibaba.fastjson.JSON;
import com.springboot.whb.study.rpc.rpc_v2.core.RpcService;
import com.springboot.whb.study.rpc.rpc_v2.domain.ServiceType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author: whb
 * @date: 2019/8/19 9:55
 * @description: 服务方配置
 */
@Data
@Slf4j
public class ServiceConfig<T> extends BasicConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private T ref;

    /**
     * 统计调用次数使用
     */
    private int count;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static <T> ServiceConfig<T> convert(String interfaceName,
                                               Class<T> interfaceClass,
                                               T ref, RpcService rpcService) {
        ServiceConfig<T> serviceConfig = new ServiceConfig<>();

        serviceConfig.setRef(ref);
        serviceConfig.setInterfaceName(interfaceName);
        serviceConfig.setInterfaceClass(interfaceClass);
        serviceConfig.setCount(0);
        serviceConfig.setMethods(MethodConfig.convert(interfaceClass.getMethods()));
        serviceConfig.setPort(rpcService.getPort());
        serviceConfig.setType(ServiceType.PROVIDER);

        try {
            InetAddress addr = InetAddress.getLocalHost();
            serviceConfig.setHost(addr.getHostAddress());
        } catch (UnknownHostException e) {
            log.error("服务方获取本机地址失败，error:{}", e);
        }
        return serviceConfig;
    }
}
