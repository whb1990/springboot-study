package com.springboot.whb.study.common.aspect;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author: whb
 * @date: 2019/7/12 10:27
 * @description: 流量限制切面
 */
@Aspect
@Component
@Scope
public class RateLimitAspect {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);
    /**
     * 每秒只发出5个令牌，此处是单进程服务的限流，内部采用令牌桶算法
     */
    private static RateLimiter rateLimiter = RateLimiter.create(5.0);

    @Pointcut("@annotation(com.springboot.whb.study.common.annotation.RateLimit)")
    public void RateLimitAspect() {

    }

    @Around("RateLimitAspect()")
    public Object around(ProceedingJoinPoint point) {
        Boolean flag = rateLimiter.tryAcquire();
        Object obj = null;
        try {
            if (flag) {
                obj = point.proceed();
            }
        } catch (Throwable e) {
            logger.error("限流失败:{}", e);
        }
        return obj;
    }
}
