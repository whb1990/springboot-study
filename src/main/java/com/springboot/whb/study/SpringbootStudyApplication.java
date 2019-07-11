package com.springboot.whb.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringbootStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootStudyApplication.class, args);
        System.out.println("====SpringBoot项目启动完毕====");
    }

}
