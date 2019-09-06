package com.springboot.whb.study.currentLimiting;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: whb
 * @date: 2019/9/6 14:23
 * @description: 漏桶限流算法
 */
public class LeakyBucket {
    /**
     * 信号量，用来达到削峰的目的，平滑流量，并且可缓存部分请求
     */
    private static final Semaphore semaphore = new Semaphore(3);
    /**
     * 线程池
     */
    public static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
            new BasicThreadFactory.Builder().namingPattern(Joiner.on("-").join("client-thread-pool-", "%s")).build());

    /**
     * 限流算法
     */
    private void semaphoreLimiter() {
        // 队列中允许存活的任务个数不能超过 5 个
        if (semaphore.getQueueLength() > 5) {
            System.out.println(LocalDateTime.now() + " - " + Thread.currentThread().getName() + " - 拒絕...");
        } else {
            try {
                semaphore.acquire();
                System.out.println(LocalDateTime.now() + " - " + Thread.currentThread().getName() + " - 通过...");
                //处理核心逻辑
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }

    /**
     * 测试限流算法
     *
     * @throws InterruptedException
     */
    public void testSemaphore() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.execute(this::semaphoreLimiter);
        }
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * main方法中执行测试方法
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        LeakyBucket leakyBucket = new LeakyBucket();
        leakyBucket.testSemaphore();
    }
}
