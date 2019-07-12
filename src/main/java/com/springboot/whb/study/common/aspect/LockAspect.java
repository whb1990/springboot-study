package com.springboot.whb.study.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: whb
 * @date: 2019/7/12 10:36
 * @description: 同步锁切面
 * <p>
 * order越小越是最先执行，但更重要的是最先执行的最后结束。order默认值是2147483647
 */
@Component
@Aspect
@Scope
@Order(1)
public class LockAspect {

    /**
     * 使用公平锁
     */
    private static Lock lock = new ReentrantLock(true);

    @Pointcut("@annotation(com.springboot.whb.study.common.annotation.LockService)")
    public void lockAspect() {

    }

    @Around("lockAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        lock.lock();
        Object obj = null;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException();
        } finally {
            lock.unlock();
        }
        return obj;
    }
}
