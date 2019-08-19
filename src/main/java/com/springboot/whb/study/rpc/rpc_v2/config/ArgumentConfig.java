package com.springboot.whb.study.rpc.rpc_v2.config;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: whb
 * @date: 2019/8/19 9:54
 * @description: 参数配置
 */
@Data
public class ArgumentConfig implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 第几个参数
     */
    private int index;

    /**
     * 参数类型
     */
    private String type;
}
