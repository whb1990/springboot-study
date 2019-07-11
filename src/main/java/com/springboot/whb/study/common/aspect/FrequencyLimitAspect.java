package com.springboot.whb.study.common.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author: whb
 * @date: 2019/7/11 14:59
 * @description: 频率限制
 */
@Aspect
@Component
public class FrequencyLimitAspect {

    private static final Logger logger = LoggerFactory.getLogger(FrequencyLimitAspect.class);

}
