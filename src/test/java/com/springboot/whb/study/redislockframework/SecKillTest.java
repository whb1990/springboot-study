package com.springboot.whb.study.redislockframework;

import com.springboot.whb.study.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;

/**
 * 模拟秒杀测试类
 */
@Slf4j
public class SecKillTest {
    /**
     * 商品1
     */
    private static Long commidityId1 = 10000001L;
    /**
     * 商品2
     */
    private static Long commidityId2 = 10000002L;
    /**
     * redis主机
     */
    public static String HOST = "127.0.0.1";
    /**
     * redis连接池
     */
    private JedisPool jedisPool;

    @Before
    public synchronized void beforeTest() {
        jedisPool = new JedisPool(HOST);
    }

    @Test
    public void testSecKill() {
        int threadCount = 1000;
        int splitPoint = 500;
        CountDownLatch endCount = new CountDownLatch(threadCount);
        CountDownLatch beginCount = new CountDownLatch(1);
        SecKillImpl testClass = new SecKillImpl();

        Thread[] threads = new Thread[threadCount];
        //起500个线程，秒杀第一个商品
        for (int i = 0; i < splitPoint; i++) {
            threads[i] = new Thread(() -> {
                try {
                    //等待在一个信号量上，挂起
                    beginCount.await();
                    //用动态代理的方式调用secKill方法
                    SeckillInterface proxy = (SeckillInterface) Proxy.newProxyInstance(SeckillInterface.class.getClassLoader(),
                            new Class[]{SeckillInterface.class}, new CacheLockInterceptor(testClass));
                    proxy.secKill("test", commidityId1);
                    endCount.countDown();
                } catch (InterruptedException e) {
                    log.error("秒杀第一个商品出错，error:{}", e);
                }
            });
            threads[i].start();
        }

        for (int i = splitPoint; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                try {
                    //等待在一个信号量上，挂起
                    beginCount.await();
                    //用动态代理的方式调用secKill方法
                    beginCount.await();
                    SeckillInterface proxy = (SeckillInterface) Proxy.newProxyInstance(SeckillInterface.class.getClassLoader(),
                            new Class[]{SeckillInterface.class}, new CacheLockInterceptor(testClass));
                    proxy.secKill("test", commidityId2);
                    endCount.countDown();
                } catch (InterruptedException e) {
                    log.error("秒杀第二个商品出错，error:{}", e);
                }
            });
            threads[i].start();
        }

        long startTime = System.currentTimeMillis();
        //主线程释放开始信号量，并等待结束信号量
        beginCount.countDown();

        try {
            //主线程等待结束信号量
            endCount.await();
            //观察秒杀结果是否正确
            System.out.println(SecKillImpl.inventory.get(commidityId1));
            System.out.println(SecKillImpl.inventory.get(commidityId2));
            System.out.println("error count" + CacheLockInterceptor.ERROR_COUNT);
            System.out.println("total cost " + DateUtils.sumTime(System.currentTimeMillis() - startTime));
        } catch (InterruptedException e) {
            log.error("秒杀失败，error:{}", e);
        }
    }
}
