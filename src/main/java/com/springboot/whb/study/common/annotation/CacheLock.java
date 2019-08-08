package com.springboot.whb.study.common.annotation;

import java.lang.annotation.*;

/**
 * 方法级注解，用于注解会产生并发问题的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheLock {

    /**
     * redis锁key的前缀
     *
     * @return
     */
    String lockedPrefix() default "";

    /**
     * 轮询锁的时间
     *
     * @return
     */
    long timeOut() default 2000;

    /**
     * key在redis里存在的时间
     *
     * @return
     */
    int expireTime() default 1000;
}
