package com.springboot.whb.study.redislockframework;

import java.lang.annotation.*;

/**
 * 参数级注解，用户注解商品ID等基本类型的参数
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockedObject {
    //不需要值
}
