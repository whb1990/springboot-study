package com.springboot.whb.study.currentLimiting;

/**
 * @author: whb
 * @date: 2019/9/6 11:59
 * @description: 滑动时间窗口限流算法
 */

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

public class TimeWindowLimited {
    /**
     * 缓存请求的队列
     */
    private ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();
    /**
     * 滑动时间窗口大小
     */
    private int seconds;
    /**
     * 最大可接受请求
     */
    private int max;

    public TimeWindowLimited(int max, int timeWindowOfSeconds) {
        this.seconds = timeWindowOfSeconds;
        this.max = max;

        new Thread(() -> {
            for (; ; ) {
                clean();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    /**
     * 获取令牌，并且添加时间
     */
    public void take() {
        long start = System.currentTimeMillis();
        try {
            int size = sizeOfValid();
            if (size > max - 1) {
                System.err.println(" 队列已满，队列容量:" + queue.size() + ",max:" + max + ",queue:" + printQueue());
                throw new IllegalStateException("full");
            }
            synchronized (queue) {
                if (sizeOfValid() > max - 1) {
                    System.err.println(" 队列已满: in synchronized,size:" + queue.size() + ",max:" + max + ",queue:" + printQueue());
                    throw new IllegalStateException("full");
                }
                this.queue.offer(System.currentTimeMillis());
            }
            System.out.println(" queue: d,size:" + queue.size() + ",max:" + max + ",queue:" + printQueue());
        } finally {
            System.out.println("耗时:" + (System.currentTimeMillis() - start) + " ms");
        }
    }

    /**
     * 打印队列内容
     *
     * @return
     */
    private String printQueue() {
        Iterator<Long> it = queue.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Long t = it.next();
            sb.append(" ").append(t);
        }
        return sb.toString();
    }

    /**
     * 有效请求
     *
     * @return
     */
    public int sizeOfValid() {
        Iterator<Long> it = queue.iterator();
        Long ms = System.currentTimeMillis() - seconds * 1000;
        int count = 0;
        while (it.hasNext()) {
            long t = it.next();
            if (t > ms) {
                count++;
            }
        }
        return count;
    }

    /**
     * 清理
     */
    public void clean() {
        Long c = System.currentTimeMillis() - seconds * 1000;
        Long tl = null;
        System.out.println("peek: " + queue.peek() + "c:" + c);
        while ((tl = queue.peek()) != null && tl < c) {
            System.out.println("peek: t:" + tl);
            queue.poll();
        }
    }

    public static void main(String[] args) {
        final TimeWindowLimited timeWindow = new TimeWindowLimited(200, 2);
        IntStream.range(0, 100).forEach((i) -> {
            new Thread(() -> {
                for (; ; ) {
                    System.out.println("before take i:" + i);
                    try {
                        Thread.sleep(new Random().nextInt(20) * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        timeWindow.take();
                        System.out.println("some option, i:" + i);
                    } catch (Exception e) {
                        System.err.println(" take i:" + i + ", encounter error:" + e.getMessage());
                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        continue;
                    }
                    System.out.println("after take i:" + i);
                }
            }).start();
        });
    }
}
