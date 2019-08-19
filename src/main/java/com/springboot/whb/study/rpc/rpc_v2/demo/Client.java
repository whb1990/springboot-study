package com.springboot.whb.study.rpc.rpc_v2.demo;

import com.google.common.base.Joiner;
import com.springboot.whb.study.rpc.rpc_v2.core.RpcClient;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: whb
 * @date: 2019/8/19 9:57
 * @description: 测试客户端
 */
public class Client {
    public static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
            new BasicThreadFactory.Builder().namingPattern(Joiner.on("-").join("client-thread-pool-", "%s")).build());

    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient();

        rpcClient.subscribe(Calculate.class);
        rpcClient.start();

        Calculate<Integer> calculateProxy = rpcClient.getInstance(Calculate.class);

        for (int i = 0; i < 200; i++) {
            threadPoolExecutor.execute(() -> {
                long start = System.currentTimeMillis();
                int s1 = new Random().nextInt(100);
                int s2 = new Random().nextInt(100);
                int s3 = calculateProxy.add(s1, s2);
                System.out.println("[" + Thread.currentThread().getName() + "]a: " + s1 + ", b:" + s2 + ", c=" + s3 + ", 耗时:" + (System.currentTimeMillis() - start));
            });
        }
    }
}
