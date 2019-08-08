package com.springboot.whb.study.common.annotation;

import java.lang.annotation.*;

/**
 * 参数级注解，用于注解自定义类型的参数
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockedComplexObject {

    /**
     * 含有成员变量的复杂对象中需要加锁的成员变量，如一个商品对象的商品ID
     *
     * @return
     */
    String filed() default "";
}
