package com.springboot.whb.study.datasource;

/**
 * @author: whb
 * @date: 2019/7/11 17:11
 * @description: 切换数据源后回调不带结果
 */
@FunctionalInterface
public interface IDataSourceChangeCallbackWithoutResult {

    void actionWithoutResult();
}
