package com.springboot.whb.study.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: whb
 * @date: 2019/7/11 17:09
 * @description: 数据源切换帮助类
 */
public class DataSourceChangeHelper {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceChangeHelper.class);

    /**
     * 切换数据源不带返回结果
     *
     * @param sourceType
     * @param action
     */
    public static void doChange(String sourceType, IDataSourceChangeCallbackWithoutResult action) {
        try {
            DynamicDataSource.setDataSource(sourceType);
            action.actionWithoutResult();
        } catch (Exception e) {
            logger.error("切换数据源出错：{}", e);
        } finally {
            DynamicDataSource.clearDataSource();
        }
    }

    /**
     * 切换数据源带返回结果
     *
     * @param sourceType
     * @param action
     * @param <T>
     * @return
     */
    public static <T> T dochangeWithResult(String sourceType, IDataSourceChangeCallback action) {
        Object result = null;
        try {
            DynamicDataSource.setDataSource(sourceType);
            result = action.actionWithResult();
        } catch (Exception e) {
            logger.error("切换数据源出错:{}", e);
        } finally {
            DynamicDataSource.clearDataSource();
        }
        return result == null ? null : (T) result;
    }

}
