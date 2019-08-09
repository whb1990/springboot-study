package com.springboot.whb.study.redislockframework;

import java.lang.annotation.*;

/**
 * 方法级注解，用于注解会产生并发问题的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheLock {
    /**
     * redis 锁key的前缀
     */
    String lockedPrefix() default "";

    /**
     * 锁时间
     */
    long timeOut() default 2000;

    /**
     * key在redis里存在的时间，1000S
     */
    int expireTime() default 100000;
}
