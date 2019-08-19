package com.springboot.whb.study.rpc.rpc_v2.balance;

import java.util.List;

/**
 * @author: whb
 * @date: 2019/8/19 9:54
 * @description: 抽象负载均衡
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String balance(List<String> addressList) {
        if (addressList == null || addressList.isEmpty()) {
            return null;
        }
        if (addressList.size() == 1) {
            return addressList.get(0);
        }
        return doLoad(addressList);
    }

    /**
     * 抽象接口，让子类去实现
     *
     * @param addressList
     * @return
     */
    abstract String doLoad(List<String> addressList);
}
