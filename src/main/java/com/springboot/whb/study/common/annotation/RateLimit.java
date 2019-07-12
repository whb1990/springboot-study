package com.springboot.whb.study.common.annotation;

import java.lang.annotation.*;

/**
 * @author: whb
 * @date: 2019/7/11 14:58
 * @description: 流量限制
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    String rateLimit() default "";
}
