package com.springboot.whb.study.rpc.rpc_v2.balance;

import java.util.List;

/**
 * @author: whb
 * @date: 2019/8/19 9:54
 * @description: 负载均衡接口定义
 */
public interface LoadBalance {

    /**
     * 负载均衡
     *
     * @param addressList
     * @return
     */
    String balance(List<String> addressList);
}
