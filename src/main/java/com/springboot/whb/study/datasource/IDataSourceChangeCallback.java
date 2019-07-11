package com.springboot.whb.study.datasource;

/**
 * @author: whb
 * @date: 2019/7/11 17:11
 * @description: 切换数据源后的回调带结果
 */
@FunctionalInterface
public interface IDataSourceChangeCallback<T> {

    T actionWithResult();
}
