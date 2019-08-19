package com.springboot.whb.study.rpc.rpc_v2.domain;

/**
 * @author: whb
 * @date: 2019/8/19 9:58
 * @description: 服务类型枚举常量
 */
public enum ServiceType {
    /**
     * 服务提供者
     */
    PROVIDER("provider"),

    /**
     * 服务消费者
     */
    CONSUMER("consumer");

    private String type;

    ServiceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
