package com.springboot.whb.study.common.interceptor;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author: whb
 * @date: 2019/7/12 14:25
 * @description: 服务启动监听
 */
public class ApplicationInitListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("=====================SpringBoot项目启动ing...===================");
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        System.out.println("=====================SpringBoot项目启动完毕===================");
        System.out.println("SpringBoot项目启动耗时：" + (end - start) + "ms");
    }
}
