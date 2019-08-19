package com.springboot.whb.study.rpc.rpc_v2.core;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: whb
 * @date: 2019/8/19 9:56
 * @description: RPC请求对象
 */
@Data
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private Object[] arguments;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
