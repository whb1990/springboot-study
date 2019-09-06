package com.springboot.whb.study.currentLimiting;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: whb
 * @date: 2019/9/6 14:57
 * @description: 计数器限流算法
 * 使用 AomicInteger 来进行统计当前正在并发执行的次数，如果超过域值就直接拒绝请求，提示系统繁忙
 */
public class CounterLimiter {
    /**
     * 计数器限流算法（比较暴力/超出直接拒绝）
     * Atomic，限制总数
     */
    private static final AtomicInteger atomic = new AtomicInteger(0);
    /**
     * 线程池
     */
    public static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
            new BasicThreadFactory.Builder().namingPattern(Joiner.on("-").join("client-thread-pool-", "%s")).build());

    /**
     * 限流
     */
    private void atomicLimiter() {
        // 最大支持 3 個
        if (atomic.get() >= 3) {
            System.out.println(LocalDateTime.now() + " - " + Thread.currentThread().getName() + " - " + "拒絕...");
        } else {
            try {
                atomic.incrementAndGet();
                //处理核心逻辑
                System.out.println(LocalDateTime.now() + " - " + Thread.currentThread().getName() + " - " + "通过...");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                atomic.decrementAndGet();
            }
        }
    }

    /**
     * 测试限流
     *
     * @throws InterruptedException
     */
    public void testAtomicLimiter() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(this::atomicLimiter);
        }
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * main方法调用
     */
    public static void main(String[] args) throws InterruptedException {
        CounterLimiter counterLimiter = new CounterLimiter();
        counterLimiter.testAtomicLimiter();
    }
}
