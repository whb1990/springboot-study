package com.springboot.whb.study.datasource.annotation;

import java.lang.annotation.*;

/**
 * @author: whb
 * @date: 2019/7/11 16:37
 * @description: 多数据源注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String name() default "";
}
