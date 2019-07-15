package com.springboot.whb.study.distributedlock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author: whb
 * @date: 2019/7/15 16:11
 * @description: Curator可重入锁测试
 */
public class CuratorInterProcessMutexTest {

    /**
     * 创建锁的路径
     */
    private static String lock_path = "/curator_lock_path";

    /**
     * Curator客户端
     */
    private static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    /**
     * 计数器
     */
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        //不加锁生成订单号--会有重复
        generatorOrderNoWithoutLock();
        //加锁生成订单号--无重复
        generatorOrderNoWithLock();
    }

    /**
     * 生成订单号-不使用锁
     */
    private static void generatorOrderNoWithoutLock() {
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                try {
                    //阻塞
                    countDownLatch.await();
                    //获取锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                String orderNo = sdf.format(new Date());
                System.out.println("不加锁生成的订单号是：" + orderNo);
            }).start();
            //唤醒
            countDownLatch.countDown();
        }
    }

    /**
     * 生成订单号--加锁
     */
    private static void generatorOrderNoWithLock() {
        client.start();
        final InterProcessMutex lock = new InterProcessMutex(client, lock_path);
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                try {
                    //阻塞
                    countDownLatch.await();
                    //获取锁
                    lock.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                String orderNo = sdf.format(new Date());
                System.out.println("生成的订单号是：" + orderNo);
                try {
                    //释放锁
                    lock.release();
                } catch (Exception e) {
                }
            }).start();
            //唤醒
            countDownLatch.countDown();
        }
    }

}
