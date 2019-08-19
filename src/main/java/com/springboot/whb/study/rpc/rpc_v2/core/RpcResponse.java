package com.springboot.whb.study.rpc.rpc_v2.core;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: whb
 * @date: 2019/8/19 9:56
 * @description: RPC响应对象
 */
@Data
public class RpcResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应结果
     */
    private T result;

    /**
     * 是否出错
     */
    private Boolean isError;

    /**
     * 错误信息
     */
    private String errorMsg;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
