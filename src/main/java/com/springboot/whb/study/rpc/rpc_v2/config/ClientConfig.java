package com.springboot.whb.study.rpc.rpc_v2.config;

import com.springboot.whb.study.rpc.rpc_v2.core.ProxyInstance;
import com.springboot.whb.study.rpc.rpc_v2.domain.ServiceType;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Proxy;

/**
 * @author: whb
 * @date: 2019/8/19 9:55
 * @description: 客户端配置
 */
@Data
public class ClientConfig<T> extends BasicConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private T proxy;

    /**
     * 反射包装成客户端参数配置对象
     *
     * @param interfaceClass
     * @param invocationHandler
     * @param <T>
     * @return
     */
    public static <T> ClientConfig<T> convert(Class<T> interfaceClass, ProxyInstance invocationHandler) {
        ClientConfig<T> config = new ClientConfig<>();

        config.setVersion("default");
        config.setInterfaceClass(interfaceClass);
        config.setInterfaceName(interfaceClass.getName());
        config.setMethods(MethodConfig.convert(interfaceClass.getMethods()));
        config.setType(ServiceType.CONSUMER);

        Object proxy = Proxy.newProxyInstance(ClientConfig.class.getClassLoader(),
                new Class<?>[]{interfaceClass},
                invocationHandler);
        config.setProxy((T) proxy);
        return config;
    }
}
