package com.springboot.whb.study.common.config;

import com.springboot.whb.study.common.interceptor.ApplicationInitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: whb
 * @date: 2019/7/12 14:27
 * @description: 监听
 */
@Configuration
public class ListenerConfig {
    @Bean
    public ApplicationInitListener applicationInitListener() {
        return new ApplicationInitListener();
    }
}
