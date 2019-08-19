package com.springboot.whb.study.rpc.rpc_v2.config;

import com.springboot.whb.study.rpc.rpc_v2.domain.ServiceType;
import lombok.Data;

import java.util.List;

/**
 * @author: whb
 * @date: 2019/8/19 9:55
 * @description: 基础配置
 */
@Data
public class BasicConfig {

    /**
     * 地址
     */
    private String host;
    /**
     * 端口号
     */
    private int port;

    /**
     * 服务提供方还是服务消费方
     */
    private ServiceType type;

    /**
     * 接口名
     */
    private String interfaceName;

    /**
     * 接口类
     */
    private Class<?> interfaceClass;

    /**
     * 方法集合
     */
    private List<MethodConfig> methods;

    /**
     * 分组
     */
    private String group;

    /**
     * 默认版本号是default
     */
    private String version = "default";

}
