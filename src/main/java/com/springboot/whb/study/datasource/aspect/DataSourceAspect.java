package com.springboot.whb.study.datasource.aspect;

import com.springboot.whb.study.datasource.DataSourceNames;
import com.springboot.whb.study.datasource.DynamicDataSource;
import com.springboot.whb.study.datasource.annotation.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author: whb
 * @date: 2019/7/11 16:38
 * @description: 多数据源切面处理类
 */
@Aspect
@Component
public class DataSourceAspect implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Pointcut("@annotation(com.springboot.whb.study.datasource.annotation.DataSource)")
    public void dataSourcePointCut() {
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (dataSource == null) {
            DynamicDataSource.setDataSource(DataSourceNames.SECKILL);
            logger.debug("set datasource is " + DataSourceNames.SECKILL);
        } else {
            DynamicDataSource.setDataSource(dataSource.name());
            logger.debug("set datasource is " + dataSource.name());
        }
        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            logger.debug("clean datasource");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
