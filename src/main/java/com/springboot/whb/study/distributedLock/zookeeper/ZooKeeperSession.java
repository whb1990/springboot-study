package com.springboot.whb.study.distributedlock.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author: whb
 * @date: 2019/7/12 22:36
 * @description: 原生Zookeeper实现分布式锁
 */
@Slf4j
public class ZooKeeperSession {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private ZooKeeper zookeeper;

    public ZooKeeperSession() {
        // 连接zookeeper server，是异步创建会话的，通过一个监听器+CountDownLatch，来确认真正建立了zk server的连接
        try {
            this.zookeeper = new ZooKeeper(
                    "localhost:2181",
                    50000,
                    new ZooKeeperWatcher());

            //打印即使状态：验证其是不是异步的？
            log.info(String.valueOf(zookeeper.getState()));

            try {
                // CountDownLatch：简而言之 初始化——非0；非0——等待；0——往下执行
                connectedSemaphore.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("ZooKeeper session established......");
        } catch (Exception e) {
            log.error("连接zookeeper失败:{}", e);
        }
    }

    /**
     * 初始化实例
     */
    public static void init() {
        getInstance();
    }

    /**
     * 建立zk session的watcher：
     */
    private class ZooKeeperWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            if (KeeperState.SyncConnected == event.getState()) {
                connectedSemaphore.countDown();
            }
        }
    }

    /**
     * 静态内部类实现单例
     */
    private static class Singleton {

        private static ZooKeeperSession instance;

        static {
            instance = new ZooKeeperSession();
        }

        public static ZooKeeperSession getInstance() {
            return instance;
        }
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static ZooKeeperSession getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 重试获取分布式锁
     *
     * @param adId
     */
    public void acquireDistributedLock(Long adId) {
        String path = "/ad-lock-" + adId;
        try {
            zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            log.info("success to acquire lock for adId = " + adId);
        } catch (Exception e) {
            // 如果锁node已经存在了，就是已经被别人加锁了，那么就这里就会报错
            // NodeExistsException
            int count = 0;
            while (true) {
                try {
                    Thread.sleep(1000);
                    zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (Exception e2) {
                    count++;
                    log.info("the " + count + " times try to acquire lock for adId = " + adId);
                    continue;
                }
                log.info("success to acquire lock for adId = " + adId + " after " + count + " times try......");
                break;
            }
        }
    }

    /**
     * 释放掉分布式锁
     *
     * @param adId
     */
    public void releaseDistributedLock(Long adId) {
        String path = "/ad-lock-" + adId;
        try {
            zookeeper.delete(path, -1);
            log.info("release the lock for adId = " + adId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Long adId = 1L;
        ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
        //1、获取锁
        zkSession.acquireDistributedLock(adId);
        //锁没有被释放，再次获取将被阻塞，直到获取成功
        //zkSession.acquireDistributedLock(adId);
        //2、执行一些修改共享资源的操作
        log.info("正在执行一些修改共享资源的操作！");

        //3、释放锁
        zkSession.releaseDistributedLock(adId);
    }
}
