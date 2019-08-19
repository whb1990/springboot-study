package com.springboot.whb.study.rpc.rpc_v2.balance;

import java.util.List;
import java.util.Random;

/**
 * @author: whb
 * @date: 2019/8/19 9:54
 * @description: 默认负载均衡--随机负载均衡
 */
public class DefaultLoadBalance extends AbstractLoadBalance {

    @Override
    String doLoad(List<String> addressList) {
        //随机
        Random random = new Random();
        return addressList.get(random.nextInt(addressList.size()));
    }
}
