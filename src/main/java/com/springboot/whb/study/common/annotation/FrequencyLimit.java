package com.springboot.whb.study.common.annotation;

import java.lang.annotation.*;

/**
 * @author: whb
 * @date: 2019/7/11 14:58
 * @description: ip 频率限制切面
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FrequencyLimit {
    String count();

    String second();
}
