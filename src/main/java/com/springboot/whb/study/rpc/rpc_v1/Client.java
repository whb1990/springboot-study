package com.springboot.whb.study.rpc.rpc_v1;

import com.google.common.base.Joiner;
import com.springboot.whb.study.rpc.rpc_v1.expore.HelloWorld;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: whb
 * @date: 2019/8/15 14:46
 * @description: 客户端
 */
public class Client {
    public static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
            new BasicThreadFactory.Builder().namingPattern(Joiner.on("-").join("client-thread-pool-", "%s")).build());

    public static void main(String[] args) {
        RpcUsedService rpcUsedService = new RpcUsedService();
        rpcUsedService.register(HelloWorld.class);
        try {
            IOClient ioClient = new IOClient("127.0.0.1", 11111);
            //网络套接字连接  同上是10001端口
            rpcUsedService.setIoClient(ioClient);
            HelloWorld helloWorld = rpcUsedService.get(HelloWorld.class);
            //生成的本地代理对象 proxy
            for (int i = 0; i < 100; i++) {
                threadPoolExecutor.execute(() -> {
                    long start = System.currentTimeMillis();
                    int a = new Random().nextInt(100);
                    int b = new Random().nextInt(100);
                    int c = helloWorld.add(a, b);
                    // .add 操作就是屏蔽了所有的细节，提供给客户端使用的方法
                    System.out.println("a: " + a + ", b:" + b + ", c=" + c + ", 耗时:" + (System.currentTimeMillis() - start));
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("客户端执行出错:{}", e);
        } finally {
            threadPoolExecutor.shutdown();
        }
    }
}
