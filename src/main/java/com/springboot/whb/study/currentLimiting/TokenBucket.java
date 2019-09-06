package com.springboot.whb.study.currentLimiting;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * @author: whb
 * @date: 2019/9/6 14:04
 * @description: 谷歌令牌桶算法测试
 */
public class TokenBucket {

    /**
     * 每秒生成2个令牌
     */
    private static final RateLimiter limiter = RateLimiter.create(2);
    /**
     * 线程池
     */
    public static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
            new BasicThreadFactory.Builder().namingPattern(Joiner.on("-").join("client-thread-pool-", "%s")).build());

    /**
     * 限流
     */
    private void rateLimiter() {
        // 默认就是 1
        final double acquire = limiter.acquire(1);
        System.out.println("当前时间 - " + LocalDateTime.now() + " - " + Thread.currentThread().getName() + " - 阻塞 - " + acquire + " 通过...");
    }

    /**
     * 限流算法测试接口
     *
     * @throws InterruptedException
     */
    public void testRateLimiter() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(this::rateLimiter);
        }
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * main方法中调用
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        TokenBucket rateLimiterTest = new TokenBucket();
        rateLimiterTest.testRateLimiter();
    }
}
